package com.gotechy.bookly.modules.catalogo.controller;

import com.gotechy.bookly.modules.catalogo.dto.ProductoRequestDTO;
import com.gotechy.bookly.modules.catalogo.model.Producto;
import com.gotechy.bookly.modules.catalogo.service.ProductoService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<Producto> crearProducto(
        @Valid @RequestBody ProductoRequestDTO productoDTO
    ) {
        Producto nuevoProducto = productoService.crearProducto(productoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarProducto(
        @PathVariable UUID id,
        @Valid @RequestBody ProductoRequestDTO productoDTO
    ) {
        Producto productoActualizado = productoService.actualizarProducto(
            id,
            productoDTO
        );

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Producto actualizado con éxito");
        respuesta.put("producto", productoActualizado);

        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarProducto(
        @PathVariable UUID id
    ) {
        productoService.eliminarProducto(id);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put(
            "mensaje",
            "Producto eliminado (inactivado) correctamente"
        );

        return ResponseEntity.ok(respuesta);
    }
}
