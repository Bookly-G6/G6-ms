package com.gotechy.bookly.modules.catalogo.model;

import jakarta.persistence.*;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipo_producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_producto_seq")
    @SequenceGenerator(name = "tipo_producto_seq", sequenceName = "tipo_producto_id_tipo_producto_seq", allocationSize = 1)
    @Column(name = "id_tipo_producto")
    private Integer idTipoProducto;

    @Column(name = "nombre_tipo", nullable = false, length = 255)
    private String nombreTipoProducto;

    @Column(name = "activa", nullable = false)
    private Boolean activa = true;
}
