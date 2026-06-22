package com.gotechy.bookly.modules.accesos.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MeResponseDTO {
    private UUID idUsuario;
    private String email;
    private String nombre;
    private String apellido;
    private String rol;
}
