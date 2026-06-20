package com.gotechy.bookly.modules.catalogo.controller;

import com.gotechy.bookly.modules.catalogo.model.TipoProducto;
import com.gotechy.bookly.modules.catalogo.service.TipoProductoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tipos-producto")
@RequiredArgsConstructor
public class TipoProductoController {

    private final TipoProductoService tipoProductoService;

    @GetMapping
    public ResponseEntity<List<TipoProducto>> listar() {
        return ResponseEntity.ok(tipoProductoService.listarActivos());
    }

    @PostMapping
    public ResponseEntity<TipoProducto> crear(
        @RequestBody TipoProducto tipoProducto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            tipoProductoService.crear(tipoProducto)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        tipoProductoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
