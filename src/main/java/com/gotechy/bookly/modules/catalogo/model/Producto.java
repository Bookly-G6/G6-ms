package com.gotechy.bookly.modules.catalogo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_producto", updatable = false, nullable = false)
    private UUID idProducto;

    @Column(name = "codigo_barras", unique = true, length = 50)
    private String codigoBarras;

    @Column(name = "nombre_producto", nullable = false)
    private String nombreProducto;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    // Costo para calcular márgenes de ganancia
    @Column(name = "precio_costo", nullable = false)
    private BigDecimal precioCosto;

    @Column(name = "precio_actual", nullable = false)
    private BigDecimal precioActual;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    // Usamos LAZY para que no traiga las tablas enteras de la base a menos que vos le pidas explícitamente el nombre

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_producto", nullable = false)
    private TipoProducto tipoProducto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_editorial_sello", nullable = false)
    private EditorialSello editorialSello;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rango_etario", nullable = false)
    private RangoEtario rangoEtario;

    // mapeo invisible

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "producto_categoria",
        joinColumns = @JoinColumn(name = "id_producto"),
        inverseJoinColumns = @JoinColumn(name = "id_categoria")
    )
    private List<Categoria> categorias;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "atributos_especificos", columnDefinition = "jsonb")
    private Map<String, Object> atributosEspecificos;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "producto_autor",
        joinColumns = @JoinColumn(name = "id_producto"),
        inverseJoinColumns = @JoinColumn(name = "id_autor_artista")
    )
    private List<AutorArtista> autores;
}
