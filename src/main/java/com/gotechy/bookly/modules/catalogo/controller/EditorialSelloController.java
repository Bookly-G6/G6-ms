package com.gotechy.bookly.modules.catalogo.controller;

import com.gotechy.bookly.modules.catalogo.model.EditorialSello;
import com.gotechy.bookly.modules.catalogo.service.EditorialSelloService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/editoriales")
@RequiredArgsConstructor
public class EditorialSelloController {

    private final EditorialSelloService editorialSelloService;

    @GetMapping
    public ResponseEntity<List<EditorialSello>> getAllEditorialSello() {
        return ResponseEntity.ok(
            editorialSelloService.listarEditorialesSelloActivas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EditorialSello> getEditorialPorId(
        @PathVariable Integer id
    ) {
        return ResponseEntity.ok(editorialSelloService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<EditorialSello> createEditorialSello(
        @Valid @RequestBody EditorialSello editorialSello
    ) {
        EditorialSello created = editorialSelloService.crearEditorialSello(
            editorialSello
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<java.util.Map<String, String>> eliminarEditorialSello(
        @PathVariable Integer id
    ) {
        editorialSelloService.eliminarEditorialSello(id);

        java.util.Map<String, String> respuesta = new java.util.HashMap<>();
        respuesta.put("mensaje", "Editorial eliminada correctamente");

        return ResponseEntity.ok(respuesta);
    }
}
