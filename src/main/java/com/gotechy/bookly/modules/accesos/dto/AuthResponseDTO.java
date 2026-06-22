package com.gotechy.bookly.modules.accesos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String email;
    private String nombre;
    private String apellido;
}
