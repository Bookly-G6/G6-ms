package com.gotechy.bookly.modules.catalogo.service;

import com.gotechy.bookly.modules.catalogo.dto.ProductoRequestDTO;
import com.gotechy.bookly.modules.catalogo.model.Producto;
import com.gotechy.bookly.modules.catalogo.repository.ProductoRepository;
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
}
