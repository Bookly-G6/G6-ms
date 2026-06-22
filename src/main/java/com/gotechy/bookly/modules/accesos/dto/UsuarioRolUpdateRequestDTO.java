package com.gotechy.bookly.modules.accesos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioRolUpdateRequestDTO {

    @NotBlank(message = "El nombre del rol es obligatorio")
    private String nombreRol;
}