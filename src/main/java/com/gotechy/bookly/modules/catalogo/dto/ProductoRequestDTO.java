package com.gotechy.bookly.modules.catalogo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductoRequestDTO(
    @NotBlank(message = "El código de barras no puede estar vacío")
    @Size(
        min = 8,
        max = 50,
        message = "El código de barras debe tener entre 8 y 50 caracteres"
    )
    String codigoBarras,

    @NotBlank(message = "El nombre del producto es obligatorio")
    String nombreProducto,

    String descripcion,

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(
        value = "0.0",
        inclusive = false,
        message = "El precio debe ser mayor a 0"
    )
    Double precioActual,

    @NotNull(message = "El ID del tipo de producto es obligatorio")
    Integer idTipoProducto,

    @NotNull(message = "El ID de la editorial/sello es obligatorio")
    Integer idEditorialSello,

    @NotNull(message = "El ID del rango etario es obligatorio")
    Integer idRangoEtario
) {}
