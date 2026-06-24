package com.gotechy.bookly.modules.logistica.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gotechy.bookly.modules.logistica.dto.ActualizarEstadoRequestDTO;
import com.gotechy.bookly.modules.logistica.dto.EnvioRequestDTO;
import com.gotechy.bookly.modules.logistica.dto.EnvioResponseDTO;
import com.gotechy.bookly.modules.logistica.services.EnvioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/envios")
@RequiredArgsConstructor
public class EnvioController {

    private final EnvioService envioService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<List<EnvioResponseDTO>> listarEnvios() {
        return ResponseEntity.ok(envioService.listarEnvios());
    }

    @GetMapping("/venta/{idVenta}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<EnvioResponseDTO> obtenerEnvioPorVenta(@PathVariable UUID idVenta) {
        return ResponseEntity.ok(envioService.obtenerPorIdVenta(idVenta));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EnvioResponseDTO> inicializarEnvio(@Valid @RequestBody EnvioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(envioService.inicializarEnvio(dto));
    }

    @PatchMapping("/{idEnvio}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EnvioResponseDTO> actualizarEstado(
        @PathVariable UUID idEnvio,
        @Valid @RequestBody ActualizarEstadoRequestDTO dto
    ) {
        return ResponseEntity.ok(envioService.actualizarEstado(
            idEnvio,
            dto.getNuevoEstado(),
            dto.getNumeroTracking(),
            dto.getEmpresaCorreo(),
            dto.getIdEmpleado()
        ));
    }
}
