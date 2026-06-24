package com.gotechy.bookly.modules.ventas.controller;

import java.util.List;
import java.util.UUID;

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

import com.gotechy.bookly.modules.ventas.dto.EmpleadoRequestDTO;
import com.gotechy.bookly.modules.ventas.dto.EmpleadoResponseDTO;
import com.gotechy.bookly.modules.ventas.service.EmpleadoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmpleadoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(empleadoService.listarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmpleadoResponseDTO> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(empleadoService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmpleadoResponseDTO> crearEmpleado(@Valid @RequestBody EmpleadoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(empleadoService.crearEmpleado(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmpleadoResponseDTO> actualizarEmpleado(@PathVariable UUID id, @Valid @RequestBody EmpleadoRequestDTO request) {
        return ResponseEntity.ok(empleadoService.actualizarEmpleado(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable UUID id) {
        empleadoService.eliminarEmpleado(id);
        return ResponseEntity.noContent().build();
    }
}
