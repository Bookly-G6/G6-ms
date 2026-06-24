package com.gotechy.bookly.modules.accesos.service;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    void deserializarRequestConNombreRolDebeMapearElRol() throws Exception {
        String json = "{\"nombre\":\"Ana\",\"apellido\":\"Pérez\",\"email\":\"ana@test.com\",\"password\":\"123456\",\"nombreRol\":\"ADMIN\"}";

        UsuarioRequestDTO request = new ObjectMapper().readValue(json, UsuarioRequestDTO.class);

        assertThat(request.getRol()).isEqualTo("ADMIN");
    }

    @Test
    void crearUsuarioDebeUsarRolEnviadoEnRequestCuandoSeProporcione() {
        UsuarioRequestDTO request = new UsuarioRequestDTO();
        request.setNombre("Ana");
        request.setApellido("Pérez");
        request.setEmail("ana@test.com");
        request.setPassword("123456");
        request.setRol("ADMIN");

        Persona persona = new Persona();
        persona.setIdPersona(UUID.randomUUID());
        persona.setNombre("Ana");
        persona.setApellido("Pérez");

        Rol rolAdmin = new Rol();
        rolAdmin.setIdRol(1);
        rolAdmin.setNombreRol("ADMIN");

        when(usuarioRepository.existsByEmail("ana@test.com")).thenReturn(false);
        when(personaRepository.saveAndFlush(any(Persona.class))).thenReturn(persona);
        when(passwordEncoder.encode("123456")).thenReturn("encoded-password");
        when(rolRepository.findByNombreRol("ADMIN")).thenReturn(Optional.of(rolAdmin));
        when(usuarioRepository.saveAndFlush(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(empleadoRepository.findByIdPersona(any(UUID.class))).thenReturn(Optional.empty());
        when(empleadoRepository.save(any(Empleado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioResponseDTO response = usuarioService.crearUsuario(request);

        assertThat(response.getRol()).isEqualTo("ADMIN");

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).saveAndFlush(usuarioCaptor.capture());
        assertThat(usuarioCaptor.getValue().getRol().getNombreRol()).isEqualTo("ADMIN");
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
