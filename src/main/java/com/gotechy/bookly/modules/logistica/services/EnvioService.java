package com.gotechy.bookly.modules.logistica.services;

import com.gotechy.bookly.core.enums.EstadoLogistica;
import com.gotechy.bookly.modules.accesos.model.Usuario;
import com.gotechy.bookly.modules.accesos.repository.UsuarioRepository;
import com.gotechy.bookly.modules.catalogo.repository.ProductoRepository;
import com.gotechy.bookly.modules.logistica.dto.*;
import com.gotechy.bookly.modules.logistica.model.Envio;
import com.gotechy.bookly.modules.logistica.model.HistorialEnvio;
import com.gotechy.bookly.modules.logistica.repository.EnvioRepository;
import com.gotechy.bookly.modules.logistica.repository.HistorialEnvioRepository;
import com.gotechy.bookly.modules.ventas.model.*;
import com.gotechy.bookly.modules.ventas.repository.*;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnvioService {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    private final EnvioRepository envioRepository;
    private final HistorialEnvioRepository historialEnvioRepository;
    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoRepository productoRepository;

    @Transactional
    public EnvioResponseDTO inicializarEnvio(EnvioRequestDTO requestDTO) {
        if (
            envioRepository
                .findByIdVentaAndActivoTrue(requestDTO.getIdVenta())
                .isPresent()
        ) {
            throw new IllegalArgumentException(
                "La venta especificada ya tiene un envío en curso."
            );
        }

        Envio nuevoEnvio = new Envio();
        nuevoEnvio.setIdVenta(requestDTO.getIdVenta());
        nuevoEnvio.setTipoEnvio(requestDTO.getTipoEnvio());
        nuevoEnvio.setEstadoLogistica(EstadoLogistica.EN_PREPARACION);
        nuevoEnvio.setObservaciones(requestDTO.getObservaciones());

        if ("RETIRO_LOCAL".equals(requestDTO.getTipoEnvio().name())) {
            nuevoEnvio.setCodigoRetiro(generarCodigoRetiro());
            nuevoEnvio.setDireccionEntrega("Sucursal Bookly (Retiro en Local)");
            nuevoEnvio.setFechaEstimadaEntrega(
                java.time.LocalDate.now().plusDays(2)
            );
        } else {
            nuevoEnvio.setFechaEstimadaEntrega(
                java.time.LocalDate.now().plusDays(7)
            );
        }

        Envio envioGuardado = envioRepository.save(nuevoEnvio);
        registrarHistorial(
            envioGuardado,
            null,
            EstadoLogistica.EN_PREPARACION,
            "Envío inicializado por el sistema",
            null
        );

        return mapearAResponseDTO(envioGuardado);
    }

    @Transactional(readOnly = true)
    public EnvioEnriquecidoDTO obtenerDetalleEnvioCompleto(UUID idEnvio) {
        // 1. Obtener Envio
        Envio envio = envioRepository
            .findById(idEnvio)
            .orElseThrow(() ->
                new EntityNotFoundException("Envío no encontrado")
            );

        // 2. Obtener Venta
        Venta venta = ventaRepository
            .findById(envio.getIdVenta())
            .orElseThrow(() ->
                new EntityNotFoundException("Venta no encontrada")
            );

        // 3. Obtener Cliente
        Cliente cliente = clienteRepository
            .findById(venta.getIdCliente())
            .orElseThrow(() ->
                new EntityNotFoundException("Cliente no encontrado")
            );

        // 4. Obtener Detalles
        List<DetalleVenta> detalles = detalleVentaRepository.findByIdVenta(
            venta.getIdVenta()
        );

        // 5. Mapear
        EnvioEnriquecidoDTO dto = new EnvioEnriquecidoDTO();
        dto.setIdEnvio(envio.getIdEnvio());
        dto.setTipoEnvio(
            envio.getTipoEnvio() != null ? envio.getTipoEnvio().name() : "N/A"
        );
        dto.setEstadoLogistica(
            envio.getEstadoLogistica() != null
                ? envio.getEstadoLogistica().name()
                : "N/A"
        );
        dto.setNumeroTracking(envio.getNumeroTracking());
        dto.setFechaEstimadaEntrega(envio.getFechaEstimadaEntrega());
        dto.setDireccionEntrega(envio.getDireccionEntrega());

        dto.setNombreCliente(
            cliente.getPersona().getNombre() +
                " " +
                cliente.getPersona().getApellido()
        );
        dto.setTelefonoCliente(cliente.getPersona().getTelefono());

        // Corrección aquí: Usamos getTotalFinal() como dice tu modelo Venta
        dto.setTotalVenta(
            venta.getTotalFinal() != null
                ? venta.getTotalFinal().doubleValue()
                : 0.0
        );

        List<DetalleProductoDTO> listaProductos = detalles
            .stream()
            .map(d -> {
                DetalleProductoDTO item = new DetalleProductoDTO();

                // Buscamos el nombre del producto usando el repo
                var producto = productoRepository
                    .findById(d.getIdProducto())
                    .orElseThrow(() ->
                        new EntityNotFoundException("Producto no encontrado")
                    );

                item.setNombreProducto(producto.getNombreProducto());
                item.setCantidad(d.getCantidad());
                // Corrección aquí: Usamos getPrecioUnitario() como dice tu modelo DetalleVenta
                item.setPrecioUnitario(
                    d.getPrecioUnitario() != null
                        ? d.getPrecioUnitario().doubleValue()
                        : 0.0
                );
                return item;
            })
            .collect(Collectors.toList());

        dto.setProductos(listaProductos);
        return dto;
    }

    @Transactional
    public EnvioResponseDTO actualizarEstado(
        UUID idEnvio,
        EstadoLogistica nuevoEstado,
        String tracking,
        String correo,
        UUID idEmpleado
    ) {
        Envio envio = envioRepository
            .findById(idEnvio)
            .orElseThrow(() ->
                new EntityNotFoundException("Envío no encontrado")
            );

        EstadoLogistica estadoAnterior = envio.getEstadoLogistica();
        envio.setEstadoLogistica(nuevoEstado);
        if (tracking != null) envio.setNumeroTracking(tracking);
        if (correo != null) envio.setEmpresaCorreo(correo);

        Envio envioActualizado = envioRepository.save(envio);
        registrarHistorial(
            envioActualizado,
            estadoAnterior,
            nuevoEstado,
            "Cambio de estado logístico",
            idEmpleado
        );

        return mapearAResponseDTO(envioActualizado);
    }

    @Transactional(readOnly = true)
    public List<EnvioResponseDTO> listarEnvios() {
        Authentication auth =
            SecurityContextHolder.getContext().getAuthentication();
        if (esAdmin(auth)) {
            return envioRepository
                .findByActivoTrue()
                .stream()
                .map(this::mapearAResponseDTO)
                .collect(Collectors.toList());
        }

        UUID idCliente = resolverIdCliente(auth.getName());
        Set<UUID> idsVentasCliente = ventaRepository
            .findByIdClienteOrderByFechaDesc(idCliente)
            .stream()
            .map(Venta::getIdVenta)
            .collect(Collectors.toSet());

        return envioRepository
            .findByIdVentaInAndActivoTrue(idsVentasCliente)
            .stream()
            .map(this::mapearAResponseDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EnvioResponseDTO obtenerPorIdVenta(UUID idVenta) {
        Envio envio = envioRepository
            .findByIdVentaAndActivoTrue(idVenta)
            .orElseThrow(() ->
                new EntityNotFoundException("No se encontró un envío activo")
            );

        Authentication auth =
            SecurityContextHolder.getContext().getAuthentication();
        if (!esAdmin(auth)) {
            verificarPropiedadVenta(idVenta, auth.getName());
        }
        return mapearAResponseDTO(envio);
    }

    @Transactional(readOnly = true)
    public Page<EnvioResponseDTO> obtenerPendientes(
        UUID idVenta,
        String terminoBusqueda,
        int page,
        int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return envioRepository
            .buscarEnviosDinamicos(
                EstadoLogistica.EN_PREPARACION,
                idVenta,
                terminoBusqueda,
                pageable
            )
            .map(this::mapearAResponseDTO);
    }

    // --- MÉTODOS PRIVADOS AUXILIARES ---

    private void verificarPropiedadVenta(UUID idVenta, String email) {
        Venta venta = ventaRepository
            .findById(idVenta)
            .orElseThrow(() ->
                new EntityNotFoundException("Venta no encontrada")
            );
        UUID idCliente = resolverIdCliente(email);
        if (!idCliente.equals(venta.getIdCliente())) {
            throw new AccessDeniedException(
                "No tienes permiso para ver el envío."
            );
        }
    }

    private UUID resolverIdCliente(String email) {
        Usuario usuario = usuarioRepository
            .findByEmail(email)
            .orElseThrow(() ->
                new EntityNotFoundException("Usuario no encontrado")
            );
        return clienteRepository
            .findByIdPersona(usuario.getPersona().getIdPersona())
            .map(Cliente::getIdCliente)
            .orElseThrow(() ->
                new AccessDeniedException("Perfil de cliente no encontrado.")
            );
    }

    private boolean esAdmin(Authentication auth) {
        return (
            auth != null &&
            auth
                .getAuthorities()
                .stream()
                .anyMatch(a -> ROLE_ADMIN.equals(a.getAuthority()))
        );
    }

    private void registrarHistorial(
        Envio envio,
        EstadoLogistica ant,
        EstadoLogistica nue,
        String obs,
        UUID emp
    ) {
        HistorialEnvio historial = new HistorialEnvio();
        historial.setEnvio(envio);
        historial.setEstadoAnterior(ant);
        historial.setEstadoNuevo(nue);
        historial.setObservaciones(obs);
        historial.setIdEmpleado(emp);
        historialEnvioRepository.save(historial);
    }

    private EnvioResponseDTO mapearAResponseDTO(Envio envio) {
        EnvioResponseDTO dto = new EnvioResponseDTO();
        dto.setIdEnvio(envio.getIdEnvio());
        dto.setIdVenta(envio.getIdVenta());
        dto.setDireccionEntrega(envio.getDireccionEntrega());
        dto.setFechaEstimadaEntrega(envio.getFechaEstimadaEntrega());
        dto.setTipoEnvio(
            envio.getTipoEnvio() != null
                ? envio.getTipoEnvio().name()
                : "NO_ASIGNADO"
        );
        dto.setEstadoLogistica(
            envio.getEstadoLogistica() != null
                ? envio.getEstadoLogistica().name()
                : "DESCONOCIDO"
        );
        dto.setCodigoRetiro(envio.getCodigoRetiro());
        dto.setEmpresaCorreo(envio.getEmpresaCorreo());
        dto.setNumeroTracking(envio.getNumeroTracking());
        dto.setFechaActualizacion(envio.getFechaActualizacion());
        return dto;
    }

    private String generarCodigoRetiro() {
        return (
            "RET-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase()
        );
    }
}
