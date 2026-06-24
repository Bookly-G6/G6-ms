package com.gotechy.bookly.modules.catalogo.controller;

import com.gotechy.bookly.core.utils.JsonAtributosValidator;
import com.gotechy.bookly.modules.catalogo.dto.HistorialPrecioResponseDTO;
import com.gotechy.bookly.modules.catalogo.dto.ProductoRequestDTO;
import com.gotechy.bookly.modules.catalogo.dto.ProductoResponseDTO;
import com.gotechy.bookly.modules.catalogo.service.ProductoService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;
    private final JsonAtributosValidator jsonValidator;

    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> listarProductos() {
        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(
            productoService.listarSegunRol(authentication)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerProductoPorId(
        @PathVariable UUID id
    ) {
        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(
            productoService.obtenerSegunRolPorId(id, authentication)
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> eliminarProducto(
        @PathVariable UUID id
    ) {
        productoService.eliminar(id);
        return ResponseEntity.ok(
            Map.of("message", "Producto eliminado correctamente")
        );
    }

    @GetMapping("/{id}/historial-precios")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<
        List<HistorialPrecioResponseDTO>
    > obtenerHistorialPrecios(@PathVariable UUID id) {
        return ResponseEntity.ok(productoService.obtenerHistorialPrecios(id));
    }
}
