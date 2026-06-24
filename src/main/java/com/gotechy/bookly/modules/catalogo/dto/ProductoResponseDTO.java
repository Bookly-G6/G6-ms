package com.gotechy.bookly.modules.catalogo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.Data;

@Data
public class ProductoResponseDTO {

    private UUID idProducto;
    private String codigoBarras;
    private String nombreProducto;
    private String descripcion;
    private BigDecimal precioCosto;
    private BigDecimal precioActual;
    private Integer stock;
    private Boolean activo;

    private String tipoProducto;
    private String editorialSello;
    private String rangoEtario;

    private List<String> categorias;

    // El JSON intacto para que rendericen la tabla de especificaciones técnicas
    private Map<String, Object> atributosEspecificos;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer stock;

    private List<String> autores;
}
