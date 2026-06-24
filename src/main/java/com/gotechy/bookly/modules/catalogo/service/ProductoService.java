package com.gotechy.bookly.modules.catalogo.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final TipoProductoRepository tipoProductoRepository;
    private final EditorialSelloRepository editorialSelloRepository;
    private final RangoEtarioRepository rangoEtarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final AutorArtistaRepository autorArtistaRepository;

    public List<ProductoResponseDTO> listarActivos() {
        return listarSegunRol(null);
    }

    public List<ProductoResponseDTO> listarSegunRol(Authentication authentication) {
        boolean esAdmin = esAdmin(authentication);

        List<Producto> productos = esAdmin
            ? productoRepository.findAll()
            : productoRepository.findByActivoTrue();

        return productos.stream()
            .map(this::mapearAResponseDTO)
            .toList();
    }

    public ProductoResponseDTO obtenerActivoPorId(UUID idProducto) {
        return obtenerSegunRolPorId(idProducto, null);
    }

    public ProductoResponseDTO obtenerSegunRolPorId(UUID idProducto, Authentication authentication) {
        UUID productoId = Objects.requireNonNull(idProducto, "El idProducto no puede ser nulo");

        Producto producto = productoRepository
            .findById(productoId)
            .orElseThrow(() ->
                new EntityNotFoundException("Producto con ID " + productoId + " no encontrado")
            );

        boolean esAdmin = esAdmin(authentication);
        boolean activo = Boolean.TRUE.equals(producto.getActivo());

        if (!activo && !esAdmin) {
            throw new EntityNotFoundException("Producto con ID " + productoId + " no encontrado");
        }

        return mapearAResponseDTO(producto);
    }

    public ProductoResponseDTO crearProducto(ProductoRequestDTO dto) {
        Integer tipoProductoId = Objects.requireNonNull(dto.getIdTipoProducto(), "El idTipoProducto es obligatorio");
        Integer editorialSelloId = Objects.requireNonNull(dto.getIdEditorialSello(), "El idEditorialSello es obligatorio");
        Integer rangoEtarioId = Objects.requireNonNull(dto.getIdRangoEtario(), "El idRangoEtario es obligatorio");
        List<Integer> categoriasIds = Objects.requireNonNull(dto.getIdsCategorias(), "Los idsCategorias son obligatorios");
        List<Integer> autoresIds = Objects.requireNonNull(dto.getIdsAutores(), "Los idsAutores son obligatorios");

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
        producto.setAtributosEspecificos(dto.getAtributosEspecificos()); // Guardado directo a JSONB

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

        // Aplanamos las relaciones para el frontend
        response.setTipoProducto(
            producto.getTipoProducto().getNombreTipoProducto()
        );
        response.setEditorialSello(
            producto.getEditorialSello().getNombreEditorial()
        );
        response.setRangoEtario(producto.getRangoEtario().getDescripcion());

        // Convertimos la List<Categoria> en una List<String>
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

        return authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(rol -> "ROLE_ADMIN".equals(rol));
    }

    public void eliminar(UUID id) {
        UUID productoId = Objects.requireNonNull(id, "El id del producto es obligatorio");
        Producto producto = productoRepository
            .findById(productoId)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "Producto con ID " + productoId + " no encontrado"
                )
            );

        producto.setActivo(false);
        productoRepository.save(producto);
    }
}
