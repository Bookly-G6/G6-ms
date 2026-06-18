package com.gotechy.bookly.modules.catalogo.model;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.Data;

@Data
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_producto")
    private UUID idProducto;

    @Column(name = "codigo_barras", unique = true)
    private String codigoBarras;

    @Column(name = "nombre_producto", nullable = false)
    private String nombreProducto;

    private String descripcion;

    @Column(name = "precio_actual", nullable = false)
    private Double precioActual;

    private Boolean activo = true;

    @Column(name = "id_tipo_producto", nullable = false)
    private Integer idTipoProducto;

    @Column(name = "id_editorial_sello", nullable = false)
    private Integer idEditorialSello;

    @Column(name = "id_rango_etario", nullable = false)
    private Integer idRangoEtario;
}
