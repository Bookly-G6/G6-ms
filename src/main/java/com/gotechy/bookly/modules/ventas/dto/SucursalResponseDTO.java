package com.gotechy.bookly.modules.ventas.dto;

import lombok.Data;

@Data
public class SucursalResponseDTO {
    private Integer idSucursal;
    private String nombre;
    private String direccion;
    private Boolean activa;
}
