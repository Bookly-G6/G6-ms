package com.gotechy.bookly.modules.ventas.dto;

import java.util.UUID;

import com.gotechy.bookly.modules.catalogo.dto.ProductoResponseDTO;

import lombok.Data;

@Data
public class InventarioResponseDTO {

    private UUID idProducto;
    private Integer stock;

    private ProductoResponseDTO producto;
}

