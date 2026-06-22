package com.gotechy.bookly.modules.ventas.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class VentaDetalleResponseDTO {
    UUID idProducto;
    String nombreProducto;
    Integer cantidad;
    BigDecimal precioUnitario;
    BigDecimal subtotalRenglon;
}
