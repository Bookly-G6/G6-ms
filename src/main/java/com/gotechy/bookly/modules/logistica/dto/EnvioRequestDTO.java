package com.gotechy.bookly.modules.logistica.dto;

import com.gotechy.bookly.core.enums.TipoEnvio;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record EnvioRequestDTO(
    @NotNull(message = "El ID de la venta es obligatorio") UUID idVenta,

    @NotNull(message = "Debe especificar si es DOMICILIO o RETIRO_SUCURSAL")
    TipoEnvio tipoEnvio,

    // Datos opcionales que vienen solo si es a Domicilio
    String empresaCorreo,
    String numeroTracking,

    String observaciones
) {}
