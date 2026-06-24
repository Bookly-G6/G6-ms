package com.gotechy.bookly.modules.accesos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email valido")
    private String email;

    @NotBlank(message = "La contrasena es obligatoria")
    @Size(min = 6, message = "La contrasena debe tener al menos 6 caracteres")
    private String password;

    private String rol;
    private String nombreRol;
    private String role;
    private String dni;
    private String telefono;
    private Boolean activo;

    public String getRol() {
        if (rol != null && !rol.isBlank()) {
            return rol;
        }
        if (nombreRol != null && !nombreRol.isBlank()) {
            return nombreRol;
        }
        return role;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public void setRole(String role) {
        this.role = role;
    }
}