package com.gotechy.bookly.modules.catalogo.service;

import com.gotechy.bookly.modules.catalogo.dto.ProductoRequestDTO;
import com.gotechy.bookly.modules.catalogo.model.Producto;
import com.gotechy.bookly.modules.catalogo.repository.ProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public Producto crearProducto(ProductoRequestDTO dto) {
        Producto producto = new Producto();
        producto.setCodigoBarras(dto.codigoBarras());
        producto.setNombreProducto(dto.nombreProducto());
        producto.setDescripcion(dto.descripcion());
        producto.setPrecioActual(dto.precioActual());
        producto.setIdTipoProducto(dto.idTipoProducto());
        producto.setIdEditorialSello(dto.idEditorialSello());
        producto.setIdRangoEtario(dto.idRangoEtario());

        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(
        UUID idProducto,
        ProductoRequestDTO dto
    ) {
        Producto productoExistente = productoRepository
            .findById(idProducto)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "No se encontró el producto con ID: " + idProducto
                )
            );

        productoExistente.setCodigoBarras(dto.codigoBarras());
        productoExistente.setNombreProducto(dto.nombreProducto());
        productoExistente.setDescripcion(dto.descripcion());
        productoExistente.setPrecioActual(dto.precioActual());
        productoExistente.setIdTipoProducto(dto.idTipoProducto());
        productoExistente.setIdEditorialSello(dto.idEditorialSello());
        productoExistente.setIdRangoEtario(dto.idRangoEtario());

        return productoRepository.save(productoExistente);
    }

    public void eliminarProducto(UUID idProducto) {
        Producto productoExistente = productoRepository
            .findById(idProducto)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "No se encontró el producto con ID: " + idProducto
                )
            );

        productoExistente.setActivo(false);
        productoRepository.save(productoExistente);
    }
}
