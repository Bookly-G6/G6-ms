package com.gotechy.bookly.modules.ventas.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gotechy.bookly.modules.accesos.model.Persona;
import com.gotechy.bookly.modules.accesos.repository.PersonaRepository;
import com.gotechy.bookly.modules.ventas.dto.ClienteRequestDTO;
import com.gotechy.bookly.modules.ventas.dto.ClienteResponseDTO;
import com.gotechy.bookly.modules.ventas.model.Cliente;
import com.gotechy.bookly.modules.ventas.repository.ClienteRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PersonaRepository personaRepository;

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(ClienteResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO obtenerPorId(UUID idCliente) {
        Cliente cliente = buscarClientePorId(idCliente);
        return ClienteResponseDTO.fromEntity(cliente);
    }

    @Transactional
    public ClienteResponseDTO crearCliente(ClienteRequestDTO request) {
        Persona persona = new Persona();
        persona.setIdPersona(UUID.randomUUID());
        persona.setNombre(request.getNombre());
        persona.setApellido(request.getApellido());
        persona.setDni(request.getDni());
        persona.setTelefono(request.getTelefono());
        persona = personaRepository.saveAndFlush(persona);

        Cliente cliente = new Cliente();
        cliente.setIdCliente(UUID.randomUUID());
        cliente.setPersona(persona);
        cliente.setPuntosFidelidad(request.getPuntosFidelidad() != null ? request.getPuntosFidelidad() : 0);

        return ClienteResponseDTO.fromEntity(clienteRepository.saveAndFlush(cliente));
    }

    @Transactional
    public ClienteResponseDTO actualizarCliente(UUID idCliente, ClienteRequestDTO request) {
        Cliente cliente = buscarClientePorId(idCliente);
        Persona persona = cliente.getPersona();
        if (persona == null) {
            persona = new Persona();
            persona.setIdPersona(UUID.randomUUID());
        }

        persona.setNombre(request.getNombre());
        persona.setApellido(request.getApellido());
        persona.setDni(request.getDni());
        persona.setTelefono(request.getTelefono());
        persona = personaRepository.saveAndFlush(persona);

        cliente.setPersona(persona);
        if (request.getPuntosFidelidad() != null) {
            cliente.setPuntosFidelidad(request.getPuntosFidelidad());
        }

        return ClienteResponseDTO.fromEntity(clienteRepository.saveAndFlush(cliente));
    }

    @Transactional
    public void eliminarCliente(UUID idCliente) {
        Cliente cliente = buscarClientePorId(idCliente);
        Persona persona = cliente.getPersona();

        clienteRepository.delete(cliente);
        if (persona != null) {
            personaRepository.delete(persona);
        }
    }

    private Cliente buscarClientePorId(UUID idCliente) {
        return clienteRepository.findById(idCliente)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con id: " + idCliente));
    }
}
