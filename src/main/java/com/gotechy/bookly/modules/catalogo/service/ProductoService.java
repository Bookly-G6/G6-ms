package com.gotechy.bookly.modules.catalogo.service;

import com.gotechy.bookly.modules.catalogo.dto.ProductoRequestDTO;
import com.gotechy.bookly.modules.catalogo.dto.ProductoResponseDTO;
import com.gotechy.bookly.modules.catalogo.model.*;
import com.gotechy.bookly.modules.catalogo.repository.*;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        return productoRepository
            .findByActivoTrue()
            .stream()
            .map(this::mapearAResponseDTO)
            .collect(Collectors.toList());
    }

    public ProductoResponseDTO crearProducto(ProductoRequestDTO dto) {
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
        if (categorias.isEmpty()) {
            throw new EntityNotFoundException(
                "No se encontraron las categorías especificadas"
            );
        }

        List<AutorArtista> autores = autorArtistaRepository.findAllById(
            dto.getIdsAutores()
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
        producto.setAtributosEspecificos(dto.getAtributosEspecificos()); // Guardado directo a JSONB

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
            .collect(Collectors.toList());
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
            .collect(Collectors.toList());
        response.setCategorias(nombresCategorias);

        response.setAtributosEspecificos(producto.getAtributosEspecificos());

        return response;
    }

    public void eliminar(UUID id) {
        Producto producto = productoRepository
            .findById(id)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "Producto con ID " + id + " no encontrado"
                )
            );

        producto.setActivo(false);
        productoRepository.save(producto);
    }
}
