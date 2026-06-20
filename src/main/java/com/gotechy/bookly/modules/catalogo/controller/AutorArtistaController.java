package com.gotechy.bookly.modules.catalogo.controller;

import com.gotechy.bookly.modules.catalogo.model.AutorArtista;
import com.gotechy.bookly.modules.catalogo.service.AutorArtistaService;
import java.util.List;
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

    @PostMapping
    public ResponseEntity<AutorArtista> crear(
        @RequestBody AutorArtista autorArtista
    ) {
        AutorArtista nuevoAutor = autorArtistaService.crear(autorArtista);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAutor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        autorArtistaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
