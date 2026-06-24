package com.gotechy.bookly.modules.accesos.service;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gotechy.bookly.modules.accesos.dto.UsuarioRequestDTO;
import com.gotechy.bookly.modules.accesos.dto.UsuarioResponseDTO;
import com.gotechy.bookly.modules.accesos.model.Persona;
import com.gotechy.bookly.modules.accesos.model.Rol;
import com.gotechy.bookly.modules.accesos.model.Usuario;
import com.gotechy.bookly.modules.accesos.repository.PersonaRepository;
import com.gotechy.bookly.modules.accesos.repository.RolRepository;
import com.gotechy.bookly.modules.accesos.repository.UsuarioRepository;
import com.gotechy.bookly.modules.ventas.model.Empleado;
import com.gotechy.bookly.modules.ventas.repository.ClienteRepository;
import com.gotechy.bookly.modules.ventas.repository.EmpleadoRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PersonaRepository personaRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void crearUsuarioDebeAsignarElRolSolicitado() {
        UUID idPersona = UUID.randomUUID();

        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setNombre("Ana");
        request.setApellido("Perez");
        request.setEmail("ana.perez@test.com");
        request.setPassword("secreto123");
        request.setNombreRol("VENDEDOR");

        Rol rolVendedor = new Rol();
        rolVendedor.setIdRol(3);
        rolVendedor.setNombreRol("VENDEDOR");

        when(usuarioRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(personaRepository.saveAndFlush(any(Persona.class))).thenAnswer(invocation -> {
            Persona persona = invocation.getArgument(0);
            persona.setIdPersona(idPersona);
            return persona;
        });
        when(rolRepository.findByNombreRol("VENDEDOR")).thenReturn(Optional.of(rolVendedor));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("password-encoded");
        when(usuarioRepository.saveAndFlush(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(empleadoRepository.findByIdPersona(idPersona)).thenReturn(Optional.empty());
        when(empleadoRepository.save(any(Empleado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioResponseDTO response = usuarioService.crearUsuario(request);

        assertEquals("VENDEDOR", response.getRol());
        verify(empleadoRepository).save(any(Empleado.class));
    }

    @Test
    void actualizarRolDebeCrearPerfilEmpleadoCuandoElRolEsAdminOVendedor() {
        UUID idUsuario = UUID.randomUUID();
        UUID idPersona = UUID.randomUUID();

        Persona persona = new Persona();
        persona.setIdPersona(idPersona);

        Rol rolAdmin = new Rol();
        rolAdmin.setIdRol(1);
        rolAdmin.setNombreRol("ADMIN");

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        usuario.setPersona(persona);
        usuario.setRol(new Rol());

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(rolRepository.findByNombreRol("ADMIN")).thenReturn(Optional.of(rolAdmin));
        when(empleadoRepository.findByIdPersona(idPersona)).thenReturn(Optional.empty());
        when(usuarioRepository.saveAndFlush(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(empleadoRepository.save(any(Empleado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        usuarioService.actualizarRol(idUsuario, "ADMIN");

        verify(empleadoRepository).save(any(Empleado.class));
    }
}
