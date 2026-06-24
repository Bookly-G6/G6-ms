package com.gotechy.bookly.modules.logistica.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class EnvioEnriquecidoDTO {

    private UUID idEnvio;
    private String tipoEnvio;
    private String estadoLogistica;
    private String numeroTracking;
    private LocalDate fechaEstimadaEntrega;
    private String direccionEntrega;

    private String nombreCliente;
    private String telefonoCliente;
    private Double totalVenta;
    private List<DetalleProductoDTO> productos;
}
