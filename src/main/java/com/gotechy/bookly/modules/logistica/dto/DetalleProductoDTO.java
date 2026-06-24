package com.gotechy.bookly.modules.logistica.dto;

import lombok.Data;

@Data
public class DetalleProductoDTO {

    private String nombreProducto;
    private Integer cantidad;
    private Double precioUnitario;
}
