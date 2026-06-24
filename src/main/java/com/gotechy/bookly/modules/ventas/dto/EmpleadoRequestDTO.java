package com.gotechy.bookly.modules.ventas.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmpleadoRequestDTO {

    @NotNull(message = "El idPersona es obligatorio")
    private UUID idPersona;

    @NotBlank(message = "El legajo es obligatorio")
    private String legajo;

    @NotBlank(message = "El cargo es obligatorio")
    private String cargo;
}
