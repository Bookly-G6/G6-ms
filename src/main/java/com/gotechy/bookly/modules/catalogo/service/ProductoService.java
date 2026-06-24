package com.gotechy.bookly.modules.catalogo.service;

import com.gotechy.bookly.modules.accesos.repository.PersonaRepository;
import com.gotechy.bookly.modules.accesos.repository.UsuarioRepository;
import com.gotechy.bookly.modules.catalogo.dto.HistorialPrecioResponseDTO;
import com.gotechy.bookly.modules.catalogo.dto.ProductoRequestDTO;
import com.gotechy.bookly.modules.catalogo.dto.ProductoResponseDTO;
import com.gotechy.bookly.modules.catalogo.model.AutorArtista;
import com.gotechy.bookly.modules.catalogo.model.Categoria;
import com.gotechy.bookly.modules.catalogo.model.EditorialSello;
import com.gotechy.bookly.modules.catalogo.model.HistorialPrecio;
import com.gotechy.bookly.modules.catalogo.model.Producto;
import com.gotechy.bookly.modules.catalogo.model.RangoEtario;
import com.gotechy.bookly.modules.catalogo.model.TipoProducto;
import com.gotechy.bookly.modules.catalogo.repository.AutorArtistaRepository;
import com.gotechy.bookly.modules.catalogo.repository.CategoriaRepository;
import com.gotechy.bookly.modules.catalogo.repository.EditorialSelloRepository;
import com.gotechy.bookly.modules.catalogo.repository.HistorialPrecioRepository;
import com.gotechy.bookly.modules.catalogo.repository.ProductoRepository;
import com.gotechy.bookly.modules.catalogo.repository.RangoEtarioRepository;
import com.gotechy.bookly.modules.catalogo.repository.TipoProductoRepository;
import com.gotechy.bookly.modules.ventas.model.Empleado;
import com.gotechy.bookly.modules.ventas.model.MovimientoStock;
import com.gotechy.bookly.modules.ventas.repository.EmpleadoRepository;
import com.gotechy.bookly.modules.ventas.repository.MovimientoStockRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.List;
import java.util.Objects;
import java.util.Objects;
import java.util.UUID;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final TipoProductoRepository tipoProductoRepository;
    private final EditorialSelloRepository editorialSelloRepository;
    private final RangoEtarioRepository rangoEtarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final AutorArtistaRepository autorArtistaRepository;
    private final HistorialPrecioRepository historialPrecioRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmpleadoRepository empleadoRepository;
    private final PersonaRepository personaRepository;
    private final MovimientoStockRepository movimientoStockRepository;

    public List<ProductoResponseDTO> listarTodos() {
        return productoRepository
            .findAll()
            .stream()
            .map(p -> mapearAResponseDTO(p))
            .toList();
    }

    public List<ProductoResponseDTO> listarActivos() {
        return listarSegunRol(null);
    }

    public List<ProductoResponseDTO> listarSegunRol(
        Authentication authentication
    ) {
        boolean esAdmin = esAdmin(authentication);

        List<Producto> productos = esAdmin
            ? productoRepository.findAll()
            : productoRepository.findByActivoTrue();

        return productos.stream().map(this::mapearAResponseDTO).toList();
    }

    public ProductoResponseDTO obtenerActivoPorId(UUID idProducto) {
        return obtenerSegunRolPorId(idProducto, null);
    }

    public ProductoResponseDTO obtenerSegunRolPorId(
        UUID idProducto,
        Authentication authentication
    ) {
        UUID productoId = Objects.requireNonNull(
            idProducto,
            "El idProducto no puede ser nulo"
        );

        Producto producto = productoRepository
            .findById(productoId)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "Producto con ID " + productoId + " no encontrado"
                )
            );

        boolean esAdmin = esAdmin(authentication);
        boolean activo = Boolean.TRUE.equals(producto.getActivo());

        if (!activo && !esAdmin) {
            throw new EntityNotFoundException(
                "Producto con ID " + productoId + " no encontrado"
            );
        }

        return mapearAResponseDTO(producto);
    }

    public ProductoResponseDTO obtenerPorId(UUID id) {
        Producto producto = productoRepository
            .findById(id)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "Producto con ID " + id + " no encontrado"
                )
            );

        if (!producto.getActivo()) {
            throw new EntityNotFoundException(
                "El producto con ID " + id + " se encuentra inactivo"
            );
        }

        return mapearAResponseDTO(producto);
    }

    public ProductoResponseDTO crearProducto(ProductoRequestDTO dto) {
        Integer tipoProductoId = Objects.requireNonNull(
            dto.getIdTipoProducto(),
            "El idTipoProducto es obligatorio"
        );
        Integer editorialSelloId = Objects.requireNonNull(
            dto.getIdEditorialSello(),
            "El idEditorialSello es obligatorio"
        );
        Integer rangoEtarioId = Objects.requireNonNull(
            dto.getIdRangoEtario(),
            "El idRangoEtario es obligatorio"
        );
        Integer stock = Objects.requireNonNull(
            dto.getStock(),
            "El stock es obligatorio"
        );
        List<Integer> categoriasIds = Objects.requireNonNull(
            dto.getIdsCategorias(),
            "Los idsCategorias son obligatorios"
        );
        List<Integer> autoresIds = Objects.requireNonNull(
            dto.getIdsAutores(),
            "Los idsAutores son obligatorios"
        );

        TipoProducto tipoProducto = tipoProductoRepository
            .findById(tipoProductoId)
            .orElseThrow(() ->
                new EntityNotFoundException("Tipo de producto no encontrado")
            );

        EditorialSello editorialSello = editorialSelloRepository
            .findById(editorialSelloId)
            .orElseThrow(() ->
                new EntityNotFoundException("Editorial o Sello no encontrado")
            );

        RangoEtario rangoEtario = rangoEtarioRepository
            .findById(rangoEtarioId)
            .orElseThrow(() ->
                new EntityNotFoundException("Rango etario no encontrado")
            );

        List<Categoria> categorias = categoriaRepository.findAllById(
            categoriasIds
        );
        if (categorias.isEmpty()) {
            throw new EntityNotFoundException(
                "No se encontraron las categorías especificadas"
            );
        }

        List<AutorArtista> autores = autorArtistaRepository.findAllById(
            autoresIds
        );
        if (autores.isEmpty()) {
            throw new EntityNotFoundException(
                "No se encontraron los autores especificados"
            );
        }

        Producto producto = new Producto();
        producto.setAutores(autores);
        producto.setCodigoBarras(dto.getCodigoBarras());
        producto.setNombreProducto(dto.getNombreProducto());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecioCosto(dto.getPrecioCosto());
        producto.setPrecioActual(dto.getPrecioActual());
        producto.setStock(Objects.requireNonNullElse(dto.getStock(), 0));
        producto.setTipoProducto(tipoProducto);
        producto.setEditorialSello(editorialSello);
        producto.setRangoEtario(rangoEtario);
        producto.setCategorias(categorias);
        producto.setAtributosEspecificos(dto.getAtributosEspecificos());

        Producto productoGuardado = productoRepository.save(producto);

        return mapearAResponseDTO(productoGuardado);
    }

    public ProductoResponseDTO mapearAResponseDTO(Producto producto) {
        ProductoResponseDTO response = new ProductoResponseDTO();
        response.setIdProducto(producto.getIdProducto());
        response.setCodigoBarras(producto.getCodigoBarras());
        response.setNombreProducto(producto.getNombreProducto());
        response.setDescripcion(producto.getDescripcion());
        response.setPrecioCosto(producto.getPrecioCosto());
        response.setPrecioActual(producto.getPrecioActual());
        response.setStock(Objects.requireNonNullElse(producto.getStock(), 0));
        response.setActivo(producto.getActivo());
        List<String> nombresAutores = producto
            .getAutores()
            .stream()
            .map(AutorArtista::getNombre)
            .toList();
        response.setAutores(nombresAutores);

        response.setTipoProducto(
            producto.getTipoProducto().getNombreTipoProducto()
        );
        response.setEditorialSello(
            producto.getEditorialSello().getNombreEditorial()
        );
        response.setRangoEtario(producto.getRangoEtario().getDescripcion());

        List<String> nombresCategorias = producto
            .getCategorias()
            .stream()
            .map(Categoria::getNombreCategoria)
            .toList();
        response.setCategorias(nombresCategorias);

        response.setAtributosEspecificos(producto.getAtributosEspecificos());

        return response;
    }

    private boolean esAdmin(Authentication authentication) {
        if (authentication == null) {
            return false;
        }

        return authentication
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(rol -> "ROLE_ADMIN".equals(rol));
    }

    private UUID obtenerIdEmpleadoAutenticado() {
        org.springframework.security.core.Authentication auth =
            org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

        if (
            auth == null ||
            !auth.isAuthenticated() ||
            "anonymousUser".equals(auth.getPrincipal())
        ) {
            throw new org.springframework.security.access.AccessDeniedException(
                "No hay un usuario autenticado para realizar esta acción."
            );
        }

        String email = auth.getName();

        com.gotechy.bookly.modules.accesos.model.Usuario usuario =
            usuarioRepository
                .findByEmail(email)
                .orElseThrow(() ->
                    new EntityNotFoundException(
                        "Usuario logueado no encontrado en la base de datos."
                    )
                );

        return empleadoRepository
            .findByIdPersona(usuario.getPersona().getIdPersona())
            .map(
                com.gotechy.bookly.modules.ventas.model.Empleado::getIdEmpleado
            )
            .orElseThrow(() ->
                new org.springframework.security.access.AccessDeniedException(
                    "El usuario logueado no tiene un perfil de Empleado asignado."
                )
            );
    }

    public void eliminar(UUID id) {
        UUID productoId = Objects.requireNonNull(
            id,
            "El id del producto es obligatorio"
        );
        Producto producto = productoRepository
            .findById(productoId)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "Producto con ID " + productoId + " no encontrado"
                )
            );

        if (!producto.getActivo()) {
            throw new IllegalStateException(
                "El producto ya se encuentra inactivo."
            );
        }

        producto.setActivo(false);
        productoRepository.save(producto);
    }

    @Transactional
    public ProductoResponseDTO actualizarProducto(
        UUID id,
        ProductoRequestDTO requestDTO
    ) {
        Producto producto = productoRepository
            .findById(id)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "Producto con ID " + id + " no encontrado"
                )
            );

        UUID idEmpleadoActual = obtenerIdEmpleadoAutenticado();

        // 1. AUDITORÍA DE STOCK
        int stockAnterior =
            producto.getStock() != null ? producto.getStock() : 0;
        int stockNuevo =
            requestDTO.getStock() != null ? requestDTO.getStock() : 0;

        if (stockAnterior != stockNuevo) {
            MovimientoStock movimiento = new MovimientoStock();
            movimiento.setIdProducto(producto.getIdProducto());
            movimiento.setCantidad(Math.abs(stockNuevo - stockAnterior));
            movimiento.setTipoMovimiento(
                stockNuevo > stockAnterior ? "INGRESO" : "EGRESO"
            );
            movimiento.setFecha(java.time.LocalDateTime.now());
            movimiento.setStockResultante(stockNuevo);
            movimiento.setIdEmpleado(idEmpleadoActual);
            movimientoStockRepository.save(movimiento);
        }

        java.math.BigDecimal costoAnt = producto.getPrecioCosto();
        java.math.BigDecimal costoNue = requestDTO.getPrecioCosto();
        java.math.BigDecimal ventaAnt = producto.getPrecioActual();
        java.math.BigDecimal ventaNue = requestDTO.getPrecioActual();

        boolean cambioPrecioCosto =
            costoAnt != null &&
            costoNue != null &&
            costoAnt.compareTo(costoNue) != 0;
        boolean cambioPrecioVenta =
            ventaAnt != null &&
            ventaNue != null &&
            ventaAnt.compareTo(ventaNue) != 0;

        if (cambioPrecioCosto || cambioPrecioVenta) {
            HistorialPrecio historial = new HistorialPrecio();
            historial.setProducto(producto);
            historial.setPrecioCostoAnterior(costoAnt);
            historial.setPrecioVentaAnterior(ventaAnt);
            historial.setPrecioCostoNuevo(costoNue);
            historial.setPrecioVentaNuevo(ventaNue);
            historial.setFechaCambio(java.time.LocalDateTime.now());
            historialPrecioRepository.save(historial);
        }

        producto.setNombreProducto(requestDTO.getNombreProducto());
        producto.setPrecioCosto(costoNue);
        producto.setPrecioActual(ventaNue);
        producto.setStock(stockNuevo);
        producto.setAtributosEspecificos(requestDTO.getAtributosEspecificos());

        return mapearAResponseDTO(productoRepository.save(producto));
    }

    public List<HistorialPrecioResponseDTO> obtenerHistorialPrecios(
        UUID idProducto
    ) {
        Producto producto = productoRepository
            .findById(idProducto)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "Producto con ID " + idProducto + " no encontrado"
                )
            );

        List<HistorialPrecio> historial =
            historialPrecioRepository.findByProducto_IdProductoOrderByFechaCambioDesc(
                idProducto
            );

        return historial
            .stream()
            .map(h -> {
                HistorialPrecioResponseDTO dto =
                    new HistorialPrecioResponseDTO();
                dto.setIdHistorial(h.getIdHistorialPrecio());
                dto.setPrecioCostoAnterior(h.getPrecioCostoAnterior());
                dto.setPrecioVentaAnterior(h.getPrecioVentaAnterior());
                dto.setPrecioCostoNuevo(h.getPrecioCostoNuevo());
                dto.setPrecioVentaNuevo(h.getPrecioVentaNuevo());
                dto.setFechaCambio(h.getFechaCambio());

                String nombreCompleto = "Sistema / Desconocido";

                if (h.getIdEmpleado() != null) {
                    empleadoRepository
                        .findById(h.getIdEmpleado())
                        .ifPresent(empleado -> {
                            personaRepository
                                .findById(empleado.getIdPersona())
                                .ifPresent(persona -> {
                                    dto.setEmpleadoNombre(
                                        persona.getNombre() +
                                            " " +
                                            persona.getApellido()
                                    );
                                });
                        });
                }

                if (dto.getEmpleadoNombre() == null) {
                    dto.setEmpleadoNombre(nombreCompleto);
                }

                return dto;
            })
            .toList();
    }
}
