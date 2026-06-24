package com.gotechy.bookly.modules.catalogo.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductoRequestDTO {

    @NotBlank(message = "El código de barras no puede estar vacío")
    @Size(
        min = 8,
        max = 50,
        message = "El código de barras debe tener entre 8 y 50 caracteres"
    )
    private String codigoBarras;

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombreProducto;

    private String descripcion;

    @NotNull(message = "El precio de costo es obligatorio")
    @DecimalMin(
        value = "0.0",
        inclusive = false,
        message = "El precio de costo debe ser mayor a 0"
    )
    private BigDecimal precioCosto;

    @NotNull(message = "El precio actual es obligatorio")
    @DecimalMin(
        value = "0.0",
        inclusive = false,
        message = "El precio actual debe ser mayor a 0"
    )
    private BigDecimal precioActual;

    @NotNull(message = "El ID del tipo de producto es obligatorio")
    private Integer idTipoProducto;

    @NotNull(message = "El ID de la editorial/sello es obligatorio")
    private Integer idEditorialSello;

    @NotNull(message = "El ID del rango etario es obligatorio")
    private Integer idRangoEtario;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock = 0;

    @NotEmpty(message = "El producto debe tener al menos una categoría")
    private List<Integer> idsCategorias;

    private Map<String, Object> atributosEspecificos;

    @NotEmpty(message = "El producto debe tener al menos un autor/artista")
    private List<Integer> idsAutores;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
}
