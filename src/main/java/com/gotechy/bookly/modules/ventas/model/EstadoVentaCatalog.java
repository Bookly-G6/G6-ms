package com.gotechy.bookly.modules.ventas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estado_venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoVentaCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estado_venta_seq")
    @SequenceGenerator(name = "estado_venta_seq", sequenceName = "estado_venta_id_estado_venta_seq", allocationSize = 1)
    @Column(name = "id_estado_venta")
    private Integer idEstadoVenta;

    @Column(name = "nombre_estado", nullable = false)
    private String nombreEstado;
}
