package com.gotechy.bookly.modules.accesos.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UsuarioUpdateRequestDTO {

    private String nombre;

    private String apellido;

    @Email(message = "Debe ser un email valido")
    private String email;

    private String dni;
    private String telefono;
    private Boolean activo;
    private String rol;

    @AssertTrue(message = "Debe enviar al menos un campo para actualizar")
    public boolean isAtLeastOneFieldPresent() {
        return nombre != null || apellido != null || email != null || 
               dni != null || telefono != null || activo != null || rol != null;
    }
}