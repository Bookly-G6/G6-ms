package com.gotechy.bookly.modules.ventas.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class VentaResponseDTO {
    UUID idVenta;
    LocalDateTime fecha;
    String estadoVenta;
    String origenVenta;
    Integer idSucursal;
    UUID idCliente;
    UUID idEmpleado;
    BigDecimal subtotalSinDescuentos;
    BigDecimal totalFinal;
    BigDecimal totalPagado;
    UUID idEnvio;
    String tipoEnvio;
    List<VentaDetalleResponseDTO> detalles;
}
