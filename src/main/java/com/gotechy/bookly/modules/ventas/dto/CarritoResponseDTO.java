package com.gotechy.bookly.modules.ventas.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarritoResponseDTO {
    private UUID idCarrito;
    private UUID idCliente;
    private List<CarritoItemResponseDTO> items;
    private BigDecimal total;
    private LocalDateTime fechaCreacion;
}
