package com.gotechy.bookly.modules.catalogo.controller;

import com.gotechy.bookly.modules.catalogo.dto.ProductoRequestDTO;
import com.gotechy.bookly.modules.catalogo.dto.ProductoResponseDTO;
import com.gotechy.bookly.modules.catalogo.service.ProductoService;
import com.gotechy.bookly.modules.catalogo.util.JsonAtributosValidator;
import jakarta.validation.Valid;
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
}
