package com.gotechy.bookly.modules.ventas.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gotechy.bookly.modules.ventas.dto.VentaCheckoutRequestDTO;
import com.gotechy.bookly.modules.ventas.dto.VentaResponseDTO;
import com.gotechy.bookly.modules.ventas.services.VentaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @PostMapping("/checkout")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE','VENDEDOR')")
    public ResponseEntity<VentaResponseDTO> checkout(@Valid @RequestBody VentaCheckoutRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaService.checkout(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<VentaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(ventaService.listarTodas());
    }

    @GetMapping("/mis-ordenes")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<VentaResponseDTO>> listarMisOrdenes() {
        return ResponseEntity.ok(ventaService.listarMisOrdenes());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<VentaResponseDTO> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(ventaService.obtenerPorId(id));
    }
}
