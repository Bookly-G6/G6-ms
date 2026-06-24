package com.gotechy.bookly.modules.ventas.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gotechy.bookly.core.enums.TipoEnvio;
import com.gotechy.bookly.modules.accesos.model.Persona;
import com.gotechy.bookly.modules.accesos.model.Usuario;
import com.gotechy.bookly.modules.accesos.repository.PersonaRepository;
import com.gotechy.bookly.modules.accesos.repository.UsuarioRepository;
import com.gotechy.bookly.modules.catalogo.model.Producto;
import com.gotechy.bookly.modules.catalogo.repository.ProductoRepository;
import com.gotechy.bookly.modules.logistica.dto.EnvioRequestDTO;
import com.gotechy.bookly.modules.logistica.dto.EnvioResponseDTO;
import com.gotechy.bookly.modules.logistica.services.EnvioService;
import com.gotechy.bookly.modules.ventas.dto.EmpleadoCatalogResponseDTO;
import com.gotechy.bookly.modules.ventas.dto.FormaPagoCatalogResponseDTO;
import com.gotechy.bookly.modules.ventas.dto.TipoVentaCatalogResponseDTO;
import com.gotechy.bookly.modules.ventas.dto.VentaCheckoutRequestDTO;
import com.gotechy.bookly.modules.ventas.dto.VentaDetalleResponseDTO;
import com.gotechy.bookly.modules.ventas.dto.VentaItemRequestDTO;
import com.gotechy.bookly.modules.ventas.dto.VentaPagoRequestDTO;
import com.gotechy.bookly.modules.ventas.dto.VentaResponseDTO;
import com.gotechy.bookly.modules.ventas.model.Cliente;
import com.gotechy.bookly.modules.ventas.model.DetalleVenta;
import com.gotechy.bookly.modules.ventas.model.Empleado;
import com.gotechy.bookly.modules.ventas.model.EstadoVentaCatalog;
import com.gotechy.bookly.modules.ventas.model.FormaPagoCatalog;
import com.gotechy.bookly.modules.ventas.model.MovimientoStock;
import com.gotechy.bookly.modules.ventas.model.Venta;
import com.gotechy.bookly.modules.ventas.model.VentaPago;
import com.gotechy.bookly.modules.ventas.repository.ClienteRepository;
import com.gotechy.bookly.modules.ventas.repository.DetalleVentaRepository;
import com.gotechy.bookly.modules.ventas.repository.EmpleadoRepository;
import com.gotechy.bookly.modules.ventas.repository.EstadoVentaCatalogRepository;
import com.gotechy.bookly.modules.ventas.repository.FormaPagoCatalogRepository;
import com.gotechy.bookly.modules.ventas.repository.MovimientoStockRepository;
import com.gotechy.bookly.modules.ventas.repository.VentaPagoRepository;
import com.gotechy.bookly.modules.ventas.repository.VentaRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VentaService {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_VENDEDOR = "ROLE_VENDEDOR";
    private static final String MOVIMIENTO_SALIDA = "SALIDA";
    private static final String ESTADO_CONFIRMADA = "CONFIRMADA";
    private static final String ORIGEN_WEB = "WEB";
    private static final String ORIGEN_LOCAL = "LOCAL";
    private static final String DNI_CONSUMIDOR_FINAL = "";
    private static final String NOMBRE_CONSUMIDOR_FINAL = "Consumidor";
    private static final String APELLIDO_CONSUMIDOR_FINAL = "Final";

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final VentaPagoRepository ventaPagoRepository;
    private final MovimientoStockRepository movimientoStockRepository;
    private final EstadoVentaCatalogRepository estadoVentaCatalogRepository;
    private final FormaPagoCatalogRepository formaPagoCatalogRepository;
    private final ClienteRepository clienteRepository;
    private final EmpleadoRepository empleadoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final EnvioService envioService;

    @Transactional
    public VentaResponseDTO checkout(VentaCheckoutRequestDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean esAdmin = esAdmin(authentication);
        boolean esVendedor = esVendedor(authentication);

        // Una sola declaración de origenVenta
        String origenVenta = Objects.requireNonNull(request.getOrigenVenta(), "El origenVenta es obligatorio")
                .trim()
                .toUpperCase();

        // Resolver cliente y empleado
        UUID idCliente = resolverIdCliente(request.getIdCliente(), authentication, esAdmin, esVendedor);
        UUID idEmpleado = resolverIdEmpleado(request.getIdEmpleado(), origenVenta);

        EstadoVentaCatalog estadoVenta = obtenerEstadoConfirmada();

        // Sobrescribir origen si es vendedor local
        if (esVendedor) {
            origenVenta = ORIGEN_LOCAL;
        }

        Venta venta = new Venta();
        venta.setFecha(LocalDateTime.now(ZoneOffset.UTC));
        venta.setOrigenVenta(origenVenta);
        venta.setIdEstadoVenta(estadoVenta.getIdEstadoVenta());
        venta.setIdCliente(idCliente);
        venta.setIdEmpleado(idEmpleado);
        venta.setSubtotalSinDescuentos(BigDecimal.ZERO);
        venta.setTotalFinal(BigDecimal.ZERO);

        Venta ventaGuardada = ventaRepository.save(venta);

        BigDecimal subtotal = BigDecimal.ZERO;
        List<DetalleVenta> detalles = new ArrayList<>();

        for (VentaItemRequestDTO item : request.getItems()) {
            UUID idProducto = Objects.requireNonNull(item.getIdProducto(), "El idProducto es obligatorio");
            Integer cantidad = Objects.requireNonNull(item.getCantidad(), "La cantidad es obligatoria");

            Producto producto = productoRepository.findById(idProducto)
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + idProducto));

            if (producto.getActivo() == null || !producto.getActivo()) {
                throw new IllegalArgumentException("El producto no está disponible: " + idProducto);
            }

            validarYDescontarStock(idProducto, cantidad, idEmpleado);

            BigDecimal precioUnitario = producto.getPrecioActual();
            BigDecimal subtotalRenglon = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
            subtotal = subtotal.add(subtotalRenglon);

            DetalleVenta detalle = new DetalleVenta();
            detalle.setIdVenta(ventaGuardada.getIdVenta());
            detalle.setIdProducto(idProducto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setIdPromocion(item.getIdPromocion());
            detalle.setSubtotalRenglon(subtotalRenglon);
            detalles.add(detalle);
        }

        detalleVentaRepository.saveAll(detalles);

        BigDecimal totalPagado = registrarPagos(ventaGuardada.getIdVenta(), request.getPagos());

        ventaGuardada.setSubtotalSinDescuentos(subtotal);
        ventaGuardada.setTotalFinal(subtotal);
        ventaGuardada = ventaRepository.save(ventaGuardada);

        if (totalPagado.compareTo(ventaGuardada.getTotalFinal()) < 0) {
            throw new IllegalArgumentException("El total abonado es menor al total de la venta");
        }

        EnvioResponseDTO envio = crearEnvioSiCorresponde(ventaGuardada, request, esVendedor);

        return construirRespuesta(ventaGuardada, detalles, totalPagado, envio);
    }

    @Transactional(readOnly = true)
    public List<TipoVentaCatalogResponseDTO> listarTiposVenta() {
        return List.of(
                TipoVentaCatalogResponseDTO.builder()
                        .codigo(ORIGEN_WEB)
                        .nombre("Venta web")
                        .descripcion("Compra online realizada por cliente autenticado")
                        .requiereEmpleado(false)
                        .generaEnvioAutomatico(true)
                        .build(),
                TipoVentaCatalogResponseDTO.builder()
                        .codigo(ORIGEN_LOCAL)
                        .nombre("Venta local")
                        .descripcion("Venta presencial atendida por personal autorizado")
                        .requiereEmpleado(true)
                        .generaEnvioAutomatico(false)
                        .build());
    }

    @Transactional(readOnly = true)
    public List<FormaPagoCatalogResponseDTO> listarFormasPago() {
        return formaPagoCatalogRepository.findAll().stream()
                .sorted(Comparator.comparing(FormaPagoCatalog::getNombrePago,
                        Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
                .map(formaPago -> FormaPagoCatalogResponseDTO.builder()
                .idFormaPago(formaPago.getIdFormaPago())
                .nombrePago(formaPago.getNombrePago())
                .build())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EmpleadoCatalogResponseDTO> listarEmpleados() {
        List<Empleado> empleados = empleadoRepository.findAll().stream()
                .sorted(Comparator.comparing(Empleado::getIdEmpleado))
                .toList();

        List<UUID> idsPersona = new ArrayList<>();
        for (Empleado empleado : empleados) {
            if (empleado.getIdPersona() != null) {
                idsPersona.add(empleado.getIdPersona());
            }
        }

        Map<UUID, Persona> personasPorId = personaRepository.findAllById(idsPersona)
                .stream()
                .collect(Collectors.toMap(Persona::getIdPersona, persona -> persona));

        return empleados.stream()
                .map(empleado -> mapearEmpleadoCatalogo(empleado, personasPorId.get(empleado.getIdPersona())))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VentaResponseDTO> listarTodas() {
        return ventaRepository.findAll().stream()
                .sorted(Comparator.comparing(Venta::getFecha).reversed())
                .map(this::construirRespuesta)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VentaResponseDTO> listarMisOrdenes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID idCliente = resolverClientePorUsuario(authentication.getName()).getIdCliente();

        return ventaRepository.findByIdClienteOrderByFechaDesc(idCliente)
                .stream()
                .map(this::construirRespuesta)
                .toList();
    }

    @Transactional(readOnly = true)
    public VentaResponseDTO obtenerPorId(UUID idVenta) {
        Venta venta = ventaRepository.findById(Objects.requireNonNull(idVenta, "El idVenta es obligatorio"))
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada: " + idVenta));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!esAdmin(authentication)) {
            UUID idClienteActual = resolverClientePorUsuario(authentication.getName()).getIdCliente();
            if (!Objects.equals(venta.getIdCliente(), idClienteActual)) {
                throw new AccessDeniedException("No tienes permiso para ver esta orden");
            }
        }

        return construirRespuesta(venta);
    }

    private UUID resolverIdCliente(UUID idClienteRequest, Authentication authentication, boolean esAdmin,
            boolean esVendedor) {
        if (esAdmin) {
            return idClienteRequest;
        }

        if (esVendedor) {
            if (idClienteRequest != null) {
                if (!clienteRepository.existsById(idClienteRequest)) {
                    throw new EntityNotFoundException("Cliente no encontrado: " + idClienteRequest);
                }
                return idClienteRequest;
            }

            return obtenerOcrearConsumidorFinal().getIdCliente();
        }

        return resolverClientePorUsuario(authentication.getName()).getIdCliente();
    }

    private UUID resolverIdEmpleado(UUID idEmpleadoRequest, String origenVenta) {
        if (idEmpleadoRequest != null && empleadoRepository.existsById(idEmpleadoRequest)) {
            return idEmpleadoRequest;
        }

        if (ORIGEN_WEB.equalsIgnoreCase(origenVenta)) {
            return null;
        }

        return empleadoRepository.findFirstByOrderByIdEmpleadoAsc()
                .map(Empleado::getIdEmpleado)
                .orElseThrow(() -> new IllegalArgumentException(
                "No existe un empleado para registrar movimientos de ventas no WEB"));
    }

    private EstadoVentaCatalog obtenerEstadoConfirmada() {
        return estadoVentaCatalogRepository.findByNombreEstadoIgnoreCase(ESTADO_CONFIRMADA)
                .orElseThrow(() -> new IllegalArgumentException("No existe el estado de venta CONFIRMADA"));
    }

    private void validarYDescontarStock(UUID idProducto, Integer cantidad, UUID idEmpleado) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + idProducto));

        int stockActual = Objects.requireNonNullElse(producto.getStock(), 0);
        if (stockActual < cantidad) {
            throw new IllegalArgumentException("Stock insuficiente para el producto: " + idProducto);
        }

        producto.setStock(stockActual - cantidad);
        productoRepository.save(producto);

        if (idEmpleado == null) {
            return;
        }

        MovimientoStock movimientoStock = new MovimientoStock();
        movimientoStock.setIdProducto(idProducto);
        movimientoStock.setCantidad(cantidad);
        movimientoStock.setTipoMovimiento(MOVIMIENTO_SALIDA);
        movimientoStock.setFecha(LocalDateTime.now(ZoneOffset.UTC));
        movimientoStock.setIdEmpleado(idEmpleado);
        movimientoStock.setStockResultante(stockActual - cantidad);
        movimientoStockRepository.save(movimientoStock);
    }

    private BigDecimal registrarPagos(UUID idVenta, List<VentaPagoRequestDTO> pagos) {
        BigDecimal totalPagado = BigDecimal.ZERO;

        for (VentaPagoRequestDTO pagoRequest : pagos) {
            Integer idFormaPago = Objects.requireNonNull(pagoRequest.getIdFormaPago(), "El idFormaPago es obligatorio");
            BigDecimal montoAbonado = Objects.requireNonNull(pagoRequest.getMontoAbonado(),
                    "El monto abonado es obligatorio");

            if (!formaPagoCatalogRepository.existsById(idFormaPago)) {
                throw new IllegalArgumentException("Forma de pago no encontrada: " + idFormaPago);
            }

            VentaPago ventaPago = new VentaPago();
            ventaPago.setIdVenta(idVenta);
            ventaPago.setIdFormaPago(idFormaPago);
            ventaPago.setMontoAbonado(montoAbonado);
            ventaPagoRepository.save(ventaPago);

            totalPagado = totalPagado.add(montoAbonado);
        }

        return totalPagado;
    }

    private EnvioResponseDTO crearEnvioSiCorresponde(Venta venta, VentaCheckoutRequestDTO request, boolean esVendedor) {
        if (esVendedor) {
            return null;
        }

        boolean origenWeb = ORIGEN_WEB.equalsIgnoreCase(venta.getOrigenVenta());
        boolean crearEnvio = Boolean.TRUE.equals(request.getGenerarEnvio()) || origenWeb;

        if (!crearEnvio) {
            return null;
        }

        TipoEnvio tipoEnvio = request.getTipoEnvio();
        if (tipoEnvio == null) {
            throw new IllegalArgumentException("Debe indicar tipoEnvio para ventas con envío");
        }

        EnvioRequestDTO envioRequestDTO = new EnvioRequestDTO();
        envioRequestDTO.setIdVenta(venta.getIdVenta());
        envioRequestDTO.setTipoEnvio(tipoEnvio);
        envioRequestDTO.setObservaciones(request.getObservacionesEnvio());

        return envioService.inicializarEnvio(envioRequestDTO);
    }

    private VentaResponseDTO construirRespuesta(Venta venta) {
        List<DetalleVenta> detalles = detalleVentaRepository.findByIdVenta(venta.getIdVenta());
        BigDecimal totalPagado = ventaPagoRepository.findByIdVenta(venta.getIdVenta())
                .stream()
                .map(VentaPago::getMontoAbonado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return construirRespuesta(venta, detalles, totalPagado, null);
    }

    private VentaResponseDTO construirRespuesta(
            Venta venta,
            List<DetalleVenta> detalles,
            BigDecimal totalPagado,
            EnvioResponseDTO envio) {
        List<VentaDetalleResponseDTO> detalleResponse = detalles.stream()
                .map(detalle -> {
                    UUID detalleProductoId = Objects.requireNonNull(detalle.getIdProducto(),
                            "El idProducto del detalle no puede ser nulo");
                    String nombreProducto = productoRepository.findById(detalleProductoId)
                            .map(Producto::getNombreProducto)
                            .orElse("Producto no disponible");

                    return VentaDetalleResponseDTO.builder()
                            .idProducto(detalleProductoId)
                            .nombreProducto(nombreProducto)
                            .cantidad(detalle.getCantidad())
                            .precioUnitario(detalle.getPrecioUnitario())
                            .subtotalRenglon(detalle.getSubtotalRenglon())
                            .build();
                })
                .toList();

        Integer estadoVentaId = Objects.requireNonNull(venta.getIdEstadoVenta(), "El idEstadoVenta no puede ser nulo");
        EstadoVentaCatalog estado = estadoVentaCatalogRepository.findById(estadoVentaId)
                .orElse(null);

        return VentaResponseDTO.builder()
                .idVenta(venta.getIdVenta())
                .fecha(venta.getFecha())
                .estadoVenta(estado == null ? null : estado.getNombreEstado())
                .origenVenta(venta.getOrigenVenta())
                .idCliente(venta.getIdCliente())
                .idEmpleado(venta.getIdEmpleado())
                .subtotalSinDescuentos(venta.getSubtotalSinDescuentos())
                .totalFinal(venta.getTotalFinal())
                .totalPagado(totalPagado)
                .idEnvio(envio == null ? null : envio.getIdEnvio())
                .tipoEnvio(envio == null ? null : envio.getTipoEnvio())
                .detalles(detalleResponse)
                .build();
    }

    private Cliente resolverClientePorUsuario(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario autenticado no encontrado"));

        return clienteRepository.findByIdPersona(usuario.getPersona().getIdPersona())
                .orElseThrow(() -> new AccessDeniedException("El usuario autenticado no tiene perfil de cliente"));
    }

    private boolean esAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> ROLE_ADMIN.equals(authority.getAuthority()));
    }

    private boolean esVendedor(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> ROLE_VENDEDOR.equals(authority.getAuthority()));
    }

    private Cliente obtenerOcrearConsumidorFinal() {
        Persona personaConsumidorFinal = personaRepository.findByDni(DNI_CONSUMIDOR_FINAL)
                .orElseGet(() -> {
                    Persona persona = new Persona();
                    persona.setIdPersona(UUID.randomUUID());
                    persona.setNombre(NOMBRE_CONSUMIDOR_FINAL);
                    persona.setApellido(APELLIDO_CONSUMIDOR_FINAL);
                    persona.setDni(DNI_CONSUMIDOR_FINAL);
                    persona.setTelefono(null);
                    return personaRepository.save(persona);
                });

        return clienteRepository.findByIdPersona(personaConsumidorFinal.getIdPersona())
                .orElseGet(() -> {
                    Cliente cliente = new Cliente();
                    cliente.setIdCliente(UUID.randomUUID());
                    cliente.setPersona(personaConsumidorFinal);
                    cliente.setPuntosFidelidad(0);
                    return clienteRepository.save(cliente);
                });
    }

    private EmpleadoCatalogResponseDTO mapearEmpleadoCatalogo(Empleado empleado, Persona persona) {
        String nombre = persona != null ? persona.getNombre() : null;
        String apellido = persona != null ? persona.getApellido() : null;
        String nombreCompleto = construirNombreCompleto(nombre, apellido);

        return EmpleadoCatalogResponseDTO.builder()
                .idEmpleado(empleado.getIdEmpleado())
                .idPersona(empleado.getIdPersona())
                .nombreCompleto(nombreCompleto)
                .nombre(nombre)
                .apellido(apellido)
                .dni(persona != null ? persona.getDni() : null)
                .telefono(persona != null ? persona.getTelefono() : null)
                .build();
    }

    private String construirNombreCompleto(String nombre, String apellido) {
        String nombreSafe = nombre == null ? "" : nombre.trim();
        String apellidoSafe = apellido == null ? "" : apellido.trim();

        String combinado = (nombreSafe + " " + apellidoSafe).trim();
        return combinado.isEmpty() ? null : combinado;
    }
}
