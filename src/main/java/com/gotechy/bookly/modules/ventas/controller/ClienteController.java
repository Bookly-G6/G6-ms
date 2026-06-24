package com.gotechy.bookly.modules.ventas.controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gotechy.bookly.modules.accesos.model.Usuario;
import com.gotechy.bookly.modules.accesos.repository.UsuarioRepository;
import com.gotechy.bookly.modules.ventas.dto.ClienteRequestDTO;
import com.gotechy.bookly.modules.ventas.dto.ClienteResponseDTO;
import com.gotechy.bookly.modules.ventas.model.Cliente;
import com.gotechy.bookly.modules.ventas.repository.ClienteRepository;
import com.gotechy.bookly.modules.ventas.services.ClienteService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'CLIENTE')")
    public ResponseEntity<ClienteResponseDTO> obtenerPorId(
        @PathVariable UUID id,
        Principal principal,
        Authentication authentication
    ) {
        if (esCliente(authentication)) {
            verificarPropiedadCliente(id, principal.getName());
        }
        return ResponseEntity.ok(clienteService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public ResponseEntity<ClienteResponseDTO> crearCliente(@Valid @RequestBody ClienteRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.crearCliente(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'CLIENTE')")
    public ResponseEntity<ClienteResponseDTO> actualizarCliente(
        @PathVariable UUID id,
        @Valid @RequestBody ClienteRequestDTO request,
        Principal principal,
        Authentication authentication
    ) {
        if (esCliente(authentication)) {
            verificarPropiedadCliente(id, principal.getName());
        }
        return ResponseEntity.ok(clienteService.actualizarCliente(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarCliente(@PathVariable UUID id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }

    private boolean esCliente(Authentication auth) {
        return auth != null && auth.getAuthorities().stream()
            .anyMatch(a -> "ROLE_CLIENTE".equals(a.getAuthority()));
    }

    private void verificarPropiedadCliente(UUID idCliente, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        Cliente clienteLogueado = clienteRepository.findByIdPersona(usuario.getPersona().getIdPersona())
            .orElseThrow(() -> new AccessDeniedException("No tienes un perfil de cliente asociado."));

        if (!clienteLogueado.getIdCliente().equals(idCliente)) {
            throw new AccessDeniedException("No tienes permiso para ver o modificar este perfil de cliente.");
        }
    }
}
