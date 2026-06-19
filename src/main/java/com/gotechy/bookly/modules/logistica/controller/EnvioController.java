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
}
