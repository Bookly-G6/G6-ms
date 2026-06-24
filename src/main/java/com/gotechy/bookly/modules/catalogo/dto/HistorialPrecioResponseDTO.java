package com.gotechy.bookly.modules.catalogo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class HistorialPrecioResponseDTO {

    private Integer idHistorial;
    private BigDecimal precioCostoAnterior;
    private BigDecimal precioVentaAnterior;
    private BigDecimal precioCostoNuevo;
    private BigDecimal precioVentaNuevo;
    private LocalDateTime fechaCambio;
    private String empleadoNombre;
}
