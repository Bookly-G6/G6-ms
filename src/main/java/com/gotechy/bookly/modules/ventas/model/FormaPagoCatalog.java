package com.gotechy.bookly.modules.ventas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "forma_pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormaPagoCatalog {

    @Id
    @Column(name = "id_forma_pago")
    private Integer idFormaPago;

    @Column(name = "nombre_pago", nullable = false)
    private String nombrePago;
}
