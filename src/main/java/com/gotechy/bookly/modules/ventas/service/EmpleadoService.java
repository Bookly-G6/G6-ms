package com.gotechy.bookly.modules.ventas.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gotechy.bookly.modules.accesos.model.Persona;
import com.gotechy.bookly.modules.accesos.repository.PersonaRepository;
import com.gotechy.bookly.modules.ventas.dto.EmpleadoRequestDTO;
import com.gotechy.bookly.modules.ventas.dto.EmpleadoResponseDTO;
import com.gotechy.bookly.modules.ventas.model.Empleado;
import com.gotechy.bookly.modules.ventas.repository.EmpleadoRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final PersonaRepository personaRepository;

    @Transactional(readOnly = true)
    public List<EmpleadoResponseDTO> listarTodos() {
        return empleadoRepository.findAll()
                .stream()
                .map(EmpleadoResponseDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmpleadoResponseDTO obtenerPorId(UUID idEmpleado) {
        Empleado empleado = buscarEmpleadoPorId(idEmpleado);
        return EmpleadoResponseDTO.fromEntity(empleado);
    }

    @Transactional
    public EmpleadoResponseDTO crearEmpleado(EmpleadoRequestDTO request) {
        Persona persona = personaRepository.findById(request.getIdPersona())
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada"));

        if (empleadoRepository.findByLegajo(request.getLegajo()).isPresent()) {
            throw new IllegalArgumentException("El legajo ya existe");
        }

        Empleado empleado = new Empleado();
        empleado.setIdEmpleado(UUID.randomUUID());
        empleado.setIdPersona(persona.getIdPersona());
        empleado.setLegajo(request.getLegajo());
        empleado.setCargo(request.getCargo());
        empleado.setIdSucursal(request.getIdSucursal());

        return EmpleadoResponseDTO.fromEntity(empleadoRepository.saveAndFlush(empleado));
    }

    @Transactional
    public EmpleadoResponseDTO actualizarEmpleado(UUID idEmpleado, EmpleadoRequestDTO request) {
        Empleado empleado = buscarEmpleadoPorId(idEmpleado);
        Persona persona = personaRepository.findById(request.getIdPersona())
                .orElseThrow(() -> new EntityNotFoundException("Persona no encontrada"));

        empleado.setIdPersona(persona.getIdPersona());
        empleado.setLegajo(request.getLegajo());
        empleado.setCargo(request.getCargo());
        empleado.setIdSucursal(request.getIdSucursal());

        return EmpleadoResponseDTO.fromEntity(empleadoRepository.saveAndFlush(empleado));
    }

    @Transactional
    public void eliminarEmpleado(UUID idEmpleado) {
        Empleado empleado = buscarEmpleadoPorId(idEmpleado);
        empleadoRepository.delete(empleado);
    }

    private Empleado buscarEmpleadoPorId(UUID idEmpleado) {
        UUID empleadoId = Objects.requireNonNull(idEmpleado, "El idEmpleado no puede ser nulo");
        return empleadoRepository.findById(empleadoId)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con id: " + idEmpleado));
    }
}
