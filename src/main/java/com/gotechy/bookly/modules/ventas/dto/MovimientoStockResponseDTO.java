package com.gotechy.bookly.modules.ventas.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class MovimientoStockResponseDTO {

    private Integer idMovimiento;
    private UUID idProducto;
    private Integer cantidad;
    private String tipoMovimiento;
    private LocalDateTime fecha;
    private UUID idEmpleado;
    private Integer stockResultante;
}
