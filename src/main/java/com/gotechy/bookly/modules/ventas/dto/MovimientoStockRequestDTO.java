package com.gotechy.bookly.modules.ventas.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MovimientoStockRequestDTO {

    private Integer idSucursal;

    @NotNull(message = "El idProducto es obligatorio")
    private UUID idProducto;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @NotBlank(message = "El tipoMovimiento es obligatorio")
    private String tipoMovimiento;

    @NotNull(message = "El idEmpleado es obligatorio")
    private UUID idEmpleado;
}
