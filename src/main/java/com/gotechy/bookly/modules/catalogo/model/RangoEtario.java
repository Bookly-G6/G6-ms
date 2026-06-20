package com.gotechy.bookly.modules.catalogo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rango_etario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RangoEtario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rango_etario")
    private Integer idRangoEtario;

    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;
}
