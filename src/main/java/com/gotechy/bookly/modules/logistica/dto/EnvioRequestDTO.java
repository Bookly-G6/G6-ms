package com.gotechy.bookly.modules.logistica.dto;

import com.gotechy.bookly.core.enums.TipoEnvio;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class EnvioRequestDTO {

    @NotNull(message = "El ID de la venta es obligatorio")
    private UUID idVenta;

    @NotNull(message = "Debe especificar si es DOMICILIO o RETIRO_SUCURSAL")
    private TipoEnvio tipoEnvio;

    private String observaciones;
}
