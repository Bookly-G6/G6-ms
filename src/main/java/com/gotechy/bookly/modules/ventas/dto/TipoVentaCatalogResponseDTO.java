package com.gotechy.bookly.modules.ventas.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TipoVentaCatalogResponseDTO {
    String codigo;
    String nombre;
    String descripcion;
    boolean requiereEmpleado;
    boolean generaEnvioAutomatico;
}
