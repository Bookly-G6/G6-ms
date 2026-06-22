package com.gotechy.bookly.modules.ventas.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VentaItemRequestDTO {

    @NotNull(message = "El idProducto es obligatorio")
    private UUID idProducto;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor o igual a 1")
    private Integer cantidad;

    private Integer idPromocion;
}
