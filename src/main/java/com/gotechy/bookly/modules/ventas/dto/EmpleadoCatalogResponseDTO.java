package com.gotechy.bookly.modules.ventas.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EmpleadoCatalogResponseDTO {
    UUID idEmpleado;
    UUID idPersona;
    String nombreCompleto;
    String nombre;
    String apellido;
    String dni;
    String telefono;
}
