package com.gotechy.bookly.modules.catalogo.controller;

import com.gotechy.bookly.modules.catalogo.model.RangoEtario;
import com.gotechy.bookly.modules.catalogo.service.RangoEtarioService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rangos-etarios")
@RequiredArgsConstructor
public class RangoEtarioController {

    private final RangoEtarioService rangoEtarioService;

    @GetMapping
    public ResponseEntity<List<RangoEtario>> listar() {
        return ResponseEntity.ok(rangoEtarioService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<RangoEtario> crear(
        @RequestBody RangoEtario rangoEtario
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            rangoEtarioService.crear(rangoEtario)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        rangoEtarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
