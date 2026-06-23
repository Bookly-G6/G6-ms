package com.gotechy.bookly.modules.ventas.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FormaPagoCatalogResponseDTO {
    Integer idFormaPago;
    String nombrePago;
}
