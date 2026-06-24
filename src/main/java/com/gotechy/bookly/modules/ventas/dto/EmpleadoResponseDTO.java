package com.gotechy.bookly.modules.ventas.dto;

import java.util.UUID;

import com.gotechy.bookly.modules.ventas.model.Empleado;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmpleadoResponseDTO {

    private UUID idEmpleado;
    private UUID idPersona;
    private String legajo;
    private String cargo;

    public static EmpleadoResponseDTO fromEntity(Empleado empleado) {
        return EmpleadoResponseDTO.builder()
                .idEmpleado(empleado.getIdEmpleado())
                .idPersona(empleado.getIdPersona())
                .legajo(empleado.getLegajo())
                .cargo(empleado.getCargo())
                .build();
    }
}
