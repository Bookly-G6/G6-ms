package com.gotechy.bookly.modules.accesos.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gotechy.bookly.modules.accesos.dto.RoleRequestDTO;
import com.gotechy.bookly.modules.accesos.dto.RoleResponseDTO;
import com.gotechy.bookly.modules.accesos.model.Rol;
import com.gotechy.bookly.modules.accesos.repository.RolRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RolRepository rolRepository;

    @Transactional(readOnly = true)
    public List<RoleResponseDTO> listarTodos() {
        return rolRepository.findAll()
                .stream()
                .map(this::mapearAResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public RoleResponseDTO obtenerPorId(Integer idRol) {
        Rol rol = buscarRolPorId(idRol);
        return mapearAResponseDTO(rol);
    }

    @Transactional
    public RoleResponseDTO crearRol(RoleRequestDTO request) {
        String nombreNormalizado = normalizarNombreRol(request.getNombreRol());

        if (rolRepository.findByNombreRol(nombreNormalizado).isPresent()) {
            throw new IllegalArgumentException("El rol ya existe: " + nombreNormalizado);
        }

        Rol nuevoRol = new Rol();
        nuevoRol.setNombreRol(nombreNormalizado);

        Rol rolGuardado = rolRepository.save(nuevoRol);
        return mapearAResponseDTO(rolGuardado);
    }

    @Transactional
    public RoleResponseDTO actualizarRol(Integer idRol, RoleRequestDTO request) {
        Rol rol = buscarRolPorId(idRol);
        String nombreNormalizado = normalizarNombreRol(request.getNombreRol());

        // Validar que no exista otro rol con el mismo nombre
        if (rolRepository.findByNombreRol(nombreNormalizado).isPresent() &&
                !rolRepository.findByNombreRol(nombreNormalizado).get().getIdRol().equals(idRol)) {
            throw new IllegalArgumentException("El rol ya existe: " + nombreNormalizado);
        }

        rol.setNombreRol(nombreNormalizado);
        Rol rolActualizado = rolRepository.save(rol);
        return mapearAResponseDTO(rolActualizado);
    }

    @Transactional
    public void eliminarRol(Integer idRol) {
        Rol rol = buscarRolPorId(idRol);
        rolRepository.delete(rol);
    }

    private Rol buscarRolPorId(Integer idRol) {
        Integer rolId = Objects.requireNonNull(idRol, "El idRol no puede ser nulo");
        return rolRepository.findById(rolId)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con id: " + idRol));
    }

    private RoleResponseDTO mapearAResponseDTO(Rol rol) {
        return RoleResponseDTO.builder()
                .idRol(rol.getIdRol())
                .nombreRol(rol.getNombreRol())
                .build();
    }

    private String normalizarNombreRol(String nombreRol) {
        return Objects.requireNonNull(nombreRol, "El nombre del rol no puede ser nulo")
                .trim()
                .toUpperCase();
    }
}
