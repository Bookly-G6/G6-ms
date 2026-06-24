package com.gotechy.bookly.modules.catalogo.controller;

import com.gotechy.bookly.modules.catalogo.model.AutorArtista;
import com.gotechy.bookly.modules.catalogo.service.AutorArtistaService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/autores")
@RequiredArgsConstructor
public class AutorArtistaController {

    private final AutorArtistaService autorArtistaService;

    @GetMapping
    public ResponseEntity<List<AutorArtista>> listar() {
        return ResponseEntity.ok(autorArtistaService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorArtista> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(autorArtistaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<AutorArtista> crear(
        @Valid @RequestBody AutorArtista autorArtista
    ) {
        AutorArtista nuevoAutor = autorArtistaService.crear(autorArtista);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAutor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorArtista> actualizar(
        @PathVariable Integer id,
        @Valid @RequestBody AutorArtista autorArtista
    ) {
        AutorArtista autorActualizado = autorArtistaService.actualizar(
            id,
            autorArtista
        );
        return ResponseEntity.ok(autorActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminar(
        @PathVariable Integer id
    ) {
        autorArtistaService.eliminar(id);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Autor/Artista eliminado correctamente");

        return ResponseEntity.ok(respuesta);
    }
}
