package com.gotechy.bookly.modules.logistica.dto;

import java.util.UUID;

import com.gotechy.bookly.core.enums.TipoEnvio;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnvioRequestDTO {

    @NotNull(message = "El ID de la venta es obligatorio")
    private UUID idVenta;

    @NotNull(message = "Debe especificar si es DOMICILIO o RETIRO_LOCAL")
    private TipoEnvio tipoEnvio;

    @NotNull(message = "La dirección de entrega es obligatoria")
    private String direccionEntrega;

    @NotNull(message = "La fecha estimada de entrega es obligatoria")
    private java.time.LocalDate fechaEstimadaEntrega;

    private String observaciones;
}
