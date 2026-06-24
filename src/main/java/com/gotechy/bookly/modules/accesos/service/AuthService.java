package com.gotechy.bookly.modules.accesos.service;

import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gotechy.bookly.config.JwtService;
import com.gotechy.bookly.modules.accesos.dto.AuthResponseDTO;
import com.gotechy.bookly.modules.accesos.dto.LoginRequestDTO;
import com.gotechy.bookly.modules.accesos.dto.MeResponseDTO;
import com.gotechy.bookly.modules.accesos.dto.RegisterRequestDTO;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ClienteRepository clienteRepository;
    private final EmpleadoRepository empleadoRepository;

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        Rol rolAsignado = resolverRolParaCreacion(request.getRol());

        Persona persona = new Persona();
        persona.setIdPersona(UUID.randomUUID());
        persona.setNombre(request.getNombre());
        persona.setApellido(request.getApellido());
        persona.setDni(request.getDni());
        persona.setTelefono(request.getTelefono());
        persona = personaRepository.saveAndFlush(persona);

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(UUID.randomUUID());
        usuario.setPersona(persona);
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(rolAsignado);
        usuario.setActivo(true);
        usuarioRepository.saveAndFlush(usuario);
        ensureProfiles(usuario);

        String token = jwtService.generateToken(usuario);
        String rolNombre = usuario.getRol() != null ? usuario.getRol().getNombreRol().trim().toUpperCase() : "CLIENTE";
        return new AuthResponseDTO(
                token,
                usuario.getIdUsuario(),
                usuario.getEmail(),
                persona.getNombre(),
                persona.getApellido(),
                rolNombre);
    }

    @Transactional
    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!usuario.getActivo()) {
            throw new IllegalArgumentException("El usuario está inactivo");
        }

        ensureProfiles(usuario);

        String token = jwtService.generateToken(usuario);
        String rolNombre = usuario.getRol() != null ? usuario.getRol().getNombreRol().trim().toUpperCase() : "CLIENTE";
        return new AuthResponseDTO(
                token,
                usuario.getIdUsuario(),
                usuario.getEmail(),
                usuario.getPersona().getNombre(),
                usuario.getPersona().getApellido(),
                rolNombre);
    }

    @Transactional(readOnly = true)
    public MeResponseDTO getCurrentUser(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Usuario no encontrado"));
        String rolNombre = usuario.getRol() != null ? usuario.getRol().getNombreRol().trim().toUpperCase() : "CLIENTE";
        return new MeResponseDTO(
                usuario.getIdUsuario(),
                usuario.getEmail(),
                usuario.getPersona().getNombre(),
                usuario.getPersona().getApellido(),
                rolNombre);
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
