package com.gotechy.bookly.modules.accesos.service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gotechy.bookly.modules.accesos.dto.UsuarioRequestDTO;
import com.gotechy.bookly.modules.accesos.dto.UsuarioResponseDTO;
import com.gotechy.bookly.modules.accesos.dto.UsuarioUpdateRequestDTO;
import com.gotechy.bookly.modules.accesos.model.Persona;
import com.gotechy.bookly.modules.accesos.model.Rol;
import com.gotechy.bookly.modules.accesos.model.Usuario;
import com.gotechy.bookly.modules.accesos.repository.PersonaRepository;
import com.gotechy.bookly.modules.accesos.repository.RolRepository;
import com.gotechy.bookly.modules.accesos.repository.UsuarioRepository;
import com.gotechy.bookly.modules.ventas.model.Cliente;
import com.gotechy.bookly.modules.ventas.model.Empleado;
import com.gotechy.bookly.modules.ventas.repository.ClienteRepository;
import com.gotechy.bookly.modules.ventas.repository.EmpleadoRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClienteRepository clienteRepository;
    private final EmpleadoRepository empleadoRepository;

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll()
            .stream()
            .map(UsuarioResponseDTO::fromEntity)
            .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerPorId(UUID idUsuario) {
        Usuario usuario = buscarUsuarioPorId(idUsuario);
        return UsuarioResponseDTO.fromEntity(usuario);
    }

    @Transactional
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya esta registrado");
        }

        Persona persona = new Persona();
        persona.setIdPersona(UUID.randomUUID());
        persona.setNombre(request.getNombre());
        persona.setApellido(request.getApellido());
        persona.setDni(request.getDni());
        persona.setTelefono(request.getTelefono());
        persona = personaRepository.saveAndFlush(persona);

        Rol rolAsignado = resolverRolParaCreacion(request.getRol());

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(UUID.randomUUID());
        usuario.setPersona(persona);
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(rolAsignado);
        usuario.setActivo(request.getActivo() == null || request.getActivo());

        Usuario usuarioGuardado = usuarioRepository.saveAndFlush(usuario);
        ensureProfiles(usuarioGuardado);
        return UsuarioResponseDTO.fromEntity(usuarioGuardado);
    }

    @Transactional
    public UsuarioResponseDTO actualizarUsuario(UUID idUsuario, UsuarioUpdateRequestDTO request) {
        Usuario usuario = buscarUsuarioPorId(idUsuario);

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (usuarioRepository.existsByEmailAndIdUsuarioNot(request.getEmail(), idUsuario)) {
                throw new IllegalArgumentException("El email ya esta registrado");
            }
            usuario.setEmail(request.getEmail());
        }

        Persona persona = usuario.getPersona();
        if (request.getNombre() != null) {
            persona.setNombre(request.getNombre());
        }
        if (request.getApellido() != null) {
            persona.setApellido(request.getApellido());
        }
        if (request.getDni() != null) {
            persona.setDni(request.getDni());
        }
        if (request.getTelefono() != null) {
            persona.setTelefono(request.getTelefono());
        }
        if (request.getActivo() != null) {
            usuario.setActivo(request.getActivo());
        }

        if (request.getRol() != null && !request.getRol().isBlank()) {
            Rol nuevoRol = resolverRolParaCreacion(request.getRol());
            usuario.setRol(nuevoRol);
            
            // Actualizar empleado si existe
            empleadoRepository.findByIdPersona(persona.getIdPersona()).ifPresent(empleado -> {
                empleado.setCargo(nuevoRol.getNombreRol());
                empleadoRepository.save(empleado);
            });
        }

        personaRepository.save(persona);
        Usuario usuarioGuardado = usuarioRepository.saveAndFlush(usuario);
        
        if (request.getRol() != null && !request.getRol().isBlank()) {
            ensureProfiles(usuarioGuardado);
        }

        return UsuarioResponseDTO.fromEntity(usuarioGuardado);
    }

    @Transactional
    public UsuarioResponseDTO actualizarRol(UUID idUsuario, String nombreRol) {
        Usuario usuario = buscarUsuarioPorId(idUsuario);
        String rolNormalizado = nombreRol == null ? null : nombreRol.trim().toUpperCase();
        Rol nuevoRol = rolRepository.findByNombreRol(rolNormalizado)
            .orElseThrow(() -> new IllegalArgumentException("El rol no existe: " + nombreRol));

        usuario.setRol(nuevoRol);
        Usuario usuarioGuardado = usuarioRepository.saveAndFlush(usuario);
        ensureProfiles(usuarioGuardado);
        return UsuarioResponseDTO.fromEntity(usuarioGuardado);
    }

    @Transactional
    public void eliminarUsuario(UUID idUsuario) {
        Usuario usuario = buscarUsuarioPorId(idUsuario);
        // Soft delete: marcar como inactivo en lugar de borrar físicamente
        usuario.setActivo(false);
        usuarioRepository.saveAndFlush(usuario);
    }

    @Transactional
    public UsuarioResponseDTO inactivarUsuario(UUID idUsuario) {
        Usuario usuario = buscarUsuarioPorId(idUsuario);
        usuario.setActivo(false);
        Usuario usuarioGuardado = usuarioRepository.saveAndFlush(usuario);
        return UsuarioResponseDTO.fromEntity(usuarioGuardado);
    }

    private Usuario buscarUsuarioPorId(UUID idUsuario) {
        UUID usuarioId = Objects.requireNonNull(idUsuario, "El idUsuario no puede ser nulo");
        return usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id: " + idUsuario));
    }

    private Rol obtenerRolCliente() {
        return rolRepository.findByNombreRol("CLIENTE")
            .orElseGet(() -> {
                Rol nuevoRol = new Rol();
                nuevoRol.setNombreRol("CLIENTE");
                return rolRepository.save(nuevoRol);
            });
    }

    private Rol resolverRolParaCreacion(String nombreRol) {
        if (nombreRol == null || nombreRol.isBlank()) {
            return obtenerRolCliente();
        }

        String rolNormalizado = nombreRol.trim().toUpperCase();
        return rolRepository.findByNombreRol(rolNormalizado)
            .orElseThrow(() -> new IllegalArgumentException("El rol no existe: " + nombreRol));
    }

    private void ensureProfiles(Usuario usuario) {
        String rolNombre = usuario.getRol() != null && usuario.getRol().getNombreRol() != null
                ? usuario.getRol().getNombreRol().trim().toUpperCase()
                : "";

        UUID idPersona = usuario.getPersona().getIdPersona();

        if ("CLIENTE".equals(rolNombre)) {
            clienteRepository.findByIdPersona(idPersona).orElseGet(() -> {
                Cliente cliente = new Cliente();
                cliente.setIdCliente(UUID.randomUUID());
                cliente.setPersona(usuario.getPersona());
                cliente.setPuntosFidelidad(0);
                return clienteRepository.save(cliente);
            });
            return;
        }

        if ("ADMIN".equals(rolNombre) || "VENDEDOR".equals(rolNombre)) {
            empleadoRepository.findByIdPersona(idPersona).orElseGet(() -> {
                Empleado empleado = new Empleado();
                empleado.setIdEmpleado(UUID.randomUUID());
                empleado.setIdPersona(idPersona);
                empleado.setLegajo("EMP-" + idPersona.toString().substring(0, 8).toUpperCase());
                empleado.setCargo(rolNombre);
                return empleadoRepository.save(empleado);
            });
        }
    }
}