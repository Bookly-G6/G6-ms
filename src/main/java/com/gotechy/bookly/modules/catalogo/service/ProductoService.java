package com.gotechy.bookly.modules.catalogo.service;

import com.gotechy.bookly.modules.catalogo.dto.ProductoRequestDTO;
import com.gotechy.bookly.modules.catalogo.dto.ProductoResponseDTO;
import com.gotechy.bookly.modules.catalogo.model.AutorArtista;
import com.gotechy.bookly.modules.catalogo.model.Categoria;
import com.gotechy.bookly.modules.catalogo.model.EditorialSello;
import com.gotechy.bookly.modules.catalogo.model.Producto;
import com.gotechy.bookly.modules.catalogo.model.RangoEtario;
import com.gotechy.bookly.modules.catalogo.model.TipoProducto;
import com.gotechy.bookly.modules.catalogo.repository.AutorArtistaRepository;
import com.gotechy.bookly.modules.catalogo.repository.CategoriaRepository;
import com.gotechy.bookly.modules.catalogo.repository.EditorialSelloRepository;
import com.gotechy.bookly.modules.catalogo.repository.ProductoRepository;
import com.gotechy.bookly.modules.catalogo.repository.RangoEtarioRepository;
import com.gotechy.bookly.modules.catalogo.repository.TipoProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
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
    // Corregida la minúscula inicial para que coincida con el uso en el método
    private final HistorialPrecioRepository historialPrecioRepository;

    public List<ProductoResponseDTO> listarActivos() {
        return productoRepository
            .findByActivoTrue()
            .stream()
            .map(this::mapearAResponseDTO)
            .toList();
    }

    public ProductoResponseDTO obtenerActivoPorId(UUID idProducto) {
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

        if (producto.getActivo() == null || !producto.getActivo()) {
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
        producto.setTipoProducto(tipoProducto);
        producto.setEditorialSello(editorialSello);
        producto.setRangoEtario(rangoEtario);
        producto.setCategorias(categorias);
        producto.setAtributosEspecificos(dto.getAtributosEspecificos());

        Producto productoGuardado = productoRepository.save(producto);

        return mapearAResponseDTO(productoGuardado);
    }

    private ProductoResponseDTO mapearAResponseDTO(Producto producto) {
        ProductoResponseDTO response = new ProductoResponseDTO();
        response.setIdProducto(producto.getIdProducto());
        response.setCodigoBarras(producto.getCodigoBarras());
        response.setNombreProducto(producto.getNombreProducto());
        response.setDescripcion(producto.getDescripcion());
        response.setPrecioCosto(producto.getPrecioCosto());
        response.setPrecioActual(producto.getPrecioActual());
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

    @Transactional
    public ProductoResponseDTO actualizarProducto(
        UUID id,
        ProductoRequestDTO dto
    ) {
        Producto producto = productoRepository
            .findById(id)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "Producto con ID " + id + " no encontrado"
                )
            );

        if (!producto.getActivo()) {
            throw new IllegalStateException(
                "No se puede editar un producto que se encuentra inactivo."
            );
        }

        java.math.BigDecimal precioCostoAnterior = producto.getPrecioCosto();
        java.math.BigDecimal precioVentaAnterior = producto.getPrecioActual();

        TipoProducto tipoProducto = tipoProductoRepository
            .findById(dto.getIdTipoProducto())
            .orElseThrow(() ->
                new EntityNotFoundException("Tipo de producto no encontrado")
            );

        EditorialSello editorialSello = editorialSelloRepository
            .findById(dto.getIdEditorialSello())
            .orElseThrow(() ->
                new EntityNotFoundException("Editorial o Sello no encontrado")
            );

        RangoEtario rangoEtario = rangoEtarioRepository
            .findById(dto.getIdRangoEtario())
            .orElseThrow(() ->
                new EntityNotFoundException("Rango etario no encontrado")
            );

        List<Categoria> categorias = categoriaRepository.findAllById(
            dto.getIdsCategorias()
        );
        if (categorias.isEmpty()) throw new EntityNotFoundException(
            "Categorías no encontradas"
        );

        List<AutorArtista> autores = autorArtistaRepository.findAllById(
            dto.getIdsAutores()
        );
        if (autores.isEmpty()) throw new EntityNotFoundException(
            "Autores no encontrados"
        );

        producto.setCodigoBarras(dto.getCodigoBarras());
        producto.setNombreProducto(dto.getNombreProducto());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecioCosto(dto.getPrecioCosto());
        producto.setPrecioActual(dto.getPrecioActual());
        producto.setAtributosEspecificos(dto.getAtributosEspecificos());

        producto.setTipoProducto(tipoProducto);
        producto.setEditorialSello(editorialSello);
        producto.setRangoEtario(rangoEtario);
        producto.setCategorias(categorias);
        producto.setAutores(autores);

        boolean cambioPrecioCosto =
            precioCostoAnterior.compareTo(dto.getPrecioCosto()) != 0;
        boolean cambioPrecioVenta =
            precioVentaAnterior.compareTo(dto.getPrecioActual()) != 0;

        if (cambioPrecioCosto || cambioPrecioVenta) {
            HistorialPrecio historial = new HistorialPrecio();
            historial.setProducto(producto);
            historial.setPrecioCostoAnterior(precioCostoAnterior);
            historial.setPrecioVentaAnterior(precioVentaAnterior);
            historial.setPrecioCostoNuevo(dto.getPrecioCosto());
            historial.setPrecioVentaNuevo(dto.getPrecioActual());
            historial.setFechaCambio(java.time.LocalDateTime.now());

            Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
            // Evitamos un posible NullPointerException si la ruta se prueba sin seguridad momentáneamente
            String username = (authentication != null)
                ? authentication.getName()
                : "e2e2e2e2-e2e2-e2e2-e2e2-e2e2e2e2e2e2";

            try {
                historial.setIdEmpleado(UUID.fromString(username));
            } catch (IllegalArgumentException e) {
                historial.setIdEmpleado(
                    UUID.fromString("e2e2e2e2-e2e2-e2e2-e2e2-e2e2e2e2e2e2")
                );
            }

            historialPrecioRepository.save(historial);
        }

        Producto productoActualizado = productoRepository.save(producto);
        return mapearAResponseDTO(productoActualizado);
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
}
