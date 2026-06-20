package com.gotechy.bookly.modules.catalogo.model;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_producto")
    private Integer idTipoProducto;

    @Column(name = "nombre_tipo", nullable = false, length = 100)
    private String nombreTipoProducto;

    @Column(name = "activa", nullable = false)
    private Boolean activa = true;
}
