package com.gotechy.bookly.modules.accesos.service;

import com.gotechy.bookly.config.JwtService;
import com.gotechy.bookly.modules.accesos.dto.AuthResponseDTO;
import com.gotechy.bookly.modules.accesos.dto.LoginRequestDTO;
import com.gotechy.bookly.modules.accesos.dto.RegisterRequestDTO;
import com.gotechy.bookly.modules.accesos.model.Persona;
import com.gotechy.bookly.modules.accesos.model.Rol;
import com.gotechy.bookly.modules.accesos.model.Usuario;
import com.gotechy.bookly.modules.accesos.repository.PersonaRepository;
import com.gotechy.bookly.modules.accesos.repository.RolRepository;
import com.gotechy.bookly.modules.accesos.repository.UsuarioRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        Rol rolCliente = rolRepository.findByNombreRol("CLIENTE")
            .orElseGet(() -> {
                Rol nuevoRol = new Rol();
                nuevoRol.setNombreRol("CLIENTE");
                return rolRepository.save(nuevoRol);
            });

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
        usuario.setRol(rolCliente);
        usuario.setActivo(true);
        usuarioRepository.saveAndFlush(usuario);

        String token = jwtService.generateToken(usuario);
        return new AuthResponseDTO(
            token,
            usuario.getEmail(),
            persona.getNombre(),
            persona.getApellido()
        );
    }

    @Transactional(readOnly = true)
    public AuthResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!usuario.getActivo()) {
            throw new IllegalArgumentException("El usuario está inactivo");
        }

        String token = jwtService.generateToken(usuario);
        return new AuthResponseDTO(
            token,
            usuario.getEmail(),
            usuario.getPersona().getNombre(),
            usuario.getPersona().getApellido()
        );
    }
}
