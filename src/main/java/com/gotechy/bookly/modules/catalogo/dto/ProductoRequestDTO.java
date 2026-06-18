package com.gotechy.bookly.modules.catalogo.dto;

public record ProductoRequestDTO(
    String codigoBarras,
    String nombreProducto,
    String descripcion,
    Double precioActual,
    Integer idTipoProducto,
    Integer idEditorialSello,
    Integer idRangoEtario
) {}
