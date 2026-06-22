package com.gotechy.bookly.modules.catalogo.model;

import jakarta.persistence.*;
import jakarta.persistence.SequenceGenerator;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rango_etario_seq")
    @SequenceGenerator(name = "rango_etario_seq", sequenceName = "rango_etario_id_rango_etario_seq", allocationSize = 1)
    @Column(name = "id_rango_etario")
    private Integer idRangoEtario;

    @Column(name = "descripcion", nullable = false, length = 255)
    private String descripcion;
}
