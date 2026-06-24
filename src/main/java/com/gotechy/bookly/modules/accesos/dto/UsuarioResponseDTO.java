package com.gotechy.bookly.modules.accesos.dto;

import com.gotechy.bookly.modules.accesos.model.Usuario;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UsuarioResponseDTO {
    UUID idUsuario;
    UUID idPersona;
    String nombre;
    String apellido;
    String dni;
    String telefono;
    String email;
    String rol;
    Boolean activo;

    public static UsuarioResponseDTO fromEntity(Usuario usuario) {
        return UsuarioResponseDTO.builder()
            .idUsuario(usuario.getIdUsuario())
            .idPersona(usuario.getPersona().getIdPersona())
            .nombre(usuario.getPersona().getNombre())
            .apellido(usuario.getPersona().getApellido())
            .dni(usuario.getPersona().getDni())
            .telefono(usuario.getPersona().getTelefono())
            .email(usuario.getEmail())
            .rol(usuario.getRol().getNombreRol())
            .activo(usuario.getActivo())
            .build();
    }
}