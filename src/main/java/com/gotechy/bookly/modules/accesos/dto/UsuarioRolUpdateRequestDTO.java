package com.gotechy.bookly.modules.accesos.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioRolUpdateRequestDTO {

    @NotBlank(message = "El nombre del rol es obligatorio")
    @JsonAlias("rol")
    private String nombreRol;
}
