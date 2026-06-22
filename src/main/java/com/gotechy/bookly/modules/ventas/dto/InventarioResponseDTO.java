package com.gotechy.bookly.modules.ventas.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class InventarioResponseDTO {

    private Integer idSucursal;
    private UUID idProducto;
    private Integer stock;
}
