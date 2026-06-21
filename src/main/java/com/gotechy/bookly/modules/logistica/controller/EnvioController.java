package com.gotechy.bookly.modules.logistica.controller;

import com.gotechy.bookly.modules.logistica.dto.ActualizarEstadoRequestDTO;
import com.gotechy.bookly.modules.logistica.dto.EnvioRequestDTO;
import com.gotechy.bookly.modules.logistica.dto.EnvioResponseDTO;
import com.gotechy.bookly.modules.logistica.services.EnvioService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/envios")
@RequiredArgsConstructor
public class EnvioController {

    private final EnvioService envioService;

    @GetMapping
    public ResponseEntity<List<EnvioResponseDTO>> listarEnvios() {
        return ResponseEntity.ok(envioService.listarEnviosActivos());
    }

    @GetMapping("/venta/{idVenta}")
    public ResponseEntity<EnvioResponseDTO> obtenerEnvioPorVenta(
        @PathVariable UUID idVenta
    ) {
        return ResponseEntity.ok(envioService.obtenerPorIdVenta(idVenta));
    }

    @PostMapping
    public ResponseEntity<EnvioResponseDTO> inicializarEnvio(
        @Valid @RequestBody EnvioRequestDTO dto
    ) {
        EnvioResponseDTO response = envioService.inicializarEnvio(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{idEnvio}/estado")
    public ResponseEntity<EnvioResponseDTO> actualizarEstado(
        @PathVariable UUID idEnvio,
        @Valid @RequestBody ActualizarEstadoRequestDTO dto
    ) {
        EnvioResponseDTO response = envioService.actualizarEstado(
            idEnvio,
            dto.getNuevoEstado(),
            dto.getNumeroTracking(),
            dto.getEmpresaCorreo(),
            dto.getIdEmpleado()
        );
        return ResponseEntity.ok(response);
    }
}
