package com.gotechy.bookly.modules.catalogo.controller;

import com.gotechy.bookly.modules.catalogo.dto.ProductoRequestDTO;
import com.gotechy.bookly.modules.catalogo.dto.ProductoResponseDTO;
import com.gotechy.bookly.modules.catalogo.service.ProductoService;
import com.gotechy.bookly.modules.catalogo.util.JsonAtributosValidator;
import jakarta.validation.Valid;
import java.util.List;
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
    private final JsonAtributosValidator jsonValidator;

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarProductos() {
        return ResponseEntity.ok(productoService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerProductoPorId(
        @PathVariable UUID id
    ) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crearProducto(
        @Valid @RequestBody ProductoRequestDTO requestDTO
    ) {
        jsonValidator.validarAtributos(requestDTO.getAtributosEspecificos());

        ProductoResponseDTO response = productoService.crearProducto(
            requestDTO
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(
        @PathVariable UUID id,
        @Valid @RequestBody ProductoRequestDTO requestDTO
    ) {
        jsonValidator.validarAtributos(requestDTO.getAtributosEspecificos());

        ProductoResponseDTO response = productoService.actualizarProducto(
            id,
            requestDTO
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<java.util.Map<String, String>> eliminarProducto(
        @PathVariable UUID id
    ) {
        productoService.eliminar(id);

        java.util.Map<String, String> respuesta = new java.util.HashMap<>();
        respuesta.put("mensaje", "Producto eliminado correctamente");

        return ResponseEntity.ok(respuesta);
    }
}
