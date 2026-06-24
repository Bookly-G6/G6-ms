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

import com.gotechy.bookly.modules.ventas.dto.InventarioResponseDTO;
import com.gotechy.bookly.modules.ventas.dto.MovimientoStockRequestDTO;
import com.gotechy.bookly.modules.ventas.dto.MovimientoStockResponseDTO;
import com.gotechy.bookly.modules.ventas.services.InventarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ResponseEntity<List<InventarioResponseDTO>> listarInventario() {
        return ResponseEntity.ok(inventarioService.listarInventario());
    }

    @GetMapping("/{idSucursal}/{idProducto}")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ResponseEntity<InventarioResponseDTO> obtenerInventario(
        @PathVariable Integer idSucursal,
        @PathVariable UUID idProducto
    ) {
        return ResponseEntity.ok(inventarioService.obtenerInventario(idSucursal, idProducto));
    }

    @GetMapping("/movimientos")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ResponseEntity<List<MovimientoStockResponseDTO>> listarMovimientos() {
        return ResponseEntity.ok(inventarioService.listarMovimientos());
    }

    @GetMapping("/movimientos/{idMovimiento}")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ResponseEntity<MovimientoStockResponseDTO> obtenerMovimiento(@PathVariable Integer idMovimiento) {
        return ResponseEntity.ok(inventarioService.obtenerMovimiento(idMovimiento));
    }

    @PostMapping("/movimientos")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ResponseEntity<MovimientoStockResponseDTO> crearMovimiento(@Valid @RequestBody MovimientoStockRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.crearMovimiento(request));
    }

    @PostMapping("/movimientos/entrada")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ResponseEntity<MovimientoStockResponseDTO> crearEntrada(@Valid @RequestBody MovimientoStockRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.crearEntrada(request));
    }

    @PostMapping("/movimientos/salida")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ResponseEntity<MovimientoStockResponseDTO> crearSalida(@Valid @RequestBody MovimientoStockRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.crearSalida(request));
    }

    @PutMapping("/movimientos/{idMovimiento}")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ResponseEntity<MovimientoStockResponseDTO> actualizarMovimiento(
        @PathVariable Integer idMovimiento,
        @Valid @RequestBody MovimientoStockRequestDTO request
    ) {
        return ResponseEntity.ok(inventarioService.actualizarMovimiento(idMovimiento, request));
    }

    @DeleteMapping("/movimientos/{idMovimiento}")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ResponseEntity<Void> eliminarMovimiento(@PathVariable Integer idMovimiento) {
        inventarioService.eliminarMovimiento(idMovimiento);
        return ResponseEntity.noContent().build();
    }
}
