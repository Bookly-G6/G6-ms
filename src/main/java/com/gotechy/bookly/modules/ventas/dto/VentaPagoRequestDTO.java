package com.gotechy.bookly.modules.ventas.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VentaPagoRequestDTO {

    @NotNull(message = "El idFormaPago es obligatorio")
    private Integer idFormaPago;

    @NotNull(message = "El montoAbonado es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto abonado debe ser mayor a 0")
    private BigDecimal montoAbonado;
}
