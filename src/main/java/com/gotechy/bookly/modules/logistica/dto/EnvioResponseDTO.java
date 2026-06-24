package com.gotechy.bookly.modules.logistica.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class EnvioResponseDTO {

    private UUID idEnvio;
    private UUID idVenta;
    private String tipoEnvio;
    private String estadoLogistica;
    private String codigoRetiro;
    private String empresaCorreo;
    private String numeroTracking;
    private LocalDateTime fechaActualizacion;
    private String direccionEntrega;
    private java.time.LocalDate fechaEstimadaEntrega;
}
