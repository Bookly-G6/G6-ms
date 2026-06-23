package com.gotechy.bookly.modules.accesos.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gotechy.bookly.modules.accesos.dto.RoleRequestDTO;
import com.gotechy.bookly.modules.accesos.dto.RoleResponseDTO;
import com.gotechy.bookly.modules.accesos.service.RoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleResponseDTO>> listarTodos() {
        return ResponseEntity.ok(roleService.listarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(roleService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleResponseDTO> crearRol(@Valid @RequestBody RoleRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(roleService.crearRol(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleResponseDTO> actualizarRol(
        @PathVariable Integer id,
        @Valid @RequestBody RoleRequestDTO request
    ) {
        return ResponseEntity.ok(roleService.actualizarRol(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarRol(@PathVariable Integer id) {
        roleService.eliminarRol(id);
        return ResponseEntity.noContent().build();
    }
}
