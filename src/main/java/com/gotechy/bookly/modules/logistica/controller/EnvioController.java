package com.gotechy.bookly.modules.logistica.controller;

import com.gotechy.bookly.modules.logistica.dto.EnvioRequestDTO;
import com.gotechy.bookly.modules.logistica.model.Envio;
import com.gotechy.bookly.modules.logistica.service.EnvioService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/logistica")
@RequiredArgsConstructor
public class EnvioController {

    private final EnvioService envioService;

    @GetMapping("/{id}")
    public ResponseEntity<Envio> obtenerEnvio(@PathVariable UUID id) {
        return ResponseEntity.ok(envioService.obtenerEnvio(id));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crearEnvio(
        @Valid @RequestBody EnvioRequestDTO dto
    ) {
        Envio nuevoEnvio = envioService.crearEnvio(dto);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Orden de logística creada con éxito");
        respuesta.put("envio", nuevoEnvio);

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarEnvio(
        @PathVariable UUID id,
        @Valid @RequestBody EnvioRequestDTO dto
    ) {
        Envio envioActualizado = envioService.actualizarEnvio(id, dto);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Envío actualizado con éxito");
        respuesta.put("envio", envioActualizado);

        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarEnvio(
        @PathVariable UUID id
    ) {
        envioService.eliminarEnvio(id);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put(
            "mensaje",
            "Envío dado de baja (inactivado) correctamente"
        );

        return ResponseEntity.ok(respuesta);
    }
}
