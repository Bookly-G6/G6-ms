package com.gotechy.bookly.modules.ventas.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gotechy.bookly.modules.ventas.dto.CarritoItemRequestDTO;
import com.gotechy.bookly.modules.ventas.dto.CarritoItemUpdateRequestDTO;
import com.gotechy.bookly.modules.ventas.dto.CarritoResponseDTO;
import com.gotechy.bookly.modules.ventas.services.CarritoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    @GetMapping("/mio")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<CarritoResponseDTO> obtenerCarritoMio() {
        return ResponseEntity.ok(carritoService.obtenerCarritoMio());
    }

    @PostMapping("/items")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<CarritoResponseDTO> agregarItem(@Valid @RequestBody CarritoItemRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carritoService.agregarItem(request));
    }

    @PatchMapping("/items/{idItem}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<CarritoResponseDTO> actualizarItem(
        @PathVariable UUID idItem,
        @Valid @RequestBody CarritoItemUpdateRequestDTO request
    ) {
        return ResponseEntity.ok(carritoService.actualizarItem(idItem, request));
    }

    @DeleteMapping("/items/{idItem}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<Void> eliminarItem(@PathVariable UUID idItem) {
        carritoService.eliminarItem(idItem);
        return ResponseEntity.noContent().build();
    }
}
