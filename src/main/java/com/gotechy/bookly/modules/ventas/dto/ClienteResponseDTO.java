package com.gotechy.bookly.modules.ventas.dto;

import java.util.UUID;

import com.gotechy.bookly.modules.ventas.model.Cliente;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ClienteResponseDTO {
    UUID idCliente;
    UUID idPersona;
    String nombre;
    String apellido;
    String dni;
    String telefono;
    Integer puntosFidelidad;

    public static ClienteResponseDTO fromEntity(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        return ClienteResponseDTO.builder()
                .idCliente(cliente.getIdCliente())
                .idPersona(cliente.getPersona() != null ? cliente.getPersona().getIdPersona() : null)
                .nombre(cliente.getPersona() != null ? cliente.getPersona().getNombre() : null)
                .apellido(cliente.getPersona() != null ? cliente.getPersona().getApellido() : null)
                .dni(cliente.getPersona() != null ? cliente.getPersona().getDni() : null)
                .telefono(cliente.getPersona() != null ? cliente.getPersona().getTelefono() : null)
                .puntosFidelidad(cliente.getPuntosFidelidad())
                .build();
    }
}
