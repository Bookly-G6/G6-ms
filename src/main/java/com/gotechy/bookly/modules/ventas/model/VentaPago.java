package com.gotechy.bookly.modules.ventas.model;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "venta_pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta_pago")
    private Integer idVentaPago;

    @Column(name = "id_venta", nullable = false)
    private UUID idVenta;

    @Column(name = "id_forma_pago", nullable = false)
    private Integer idFormaPago;

    @Column(name = "monto_abonado", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoAbonado;
}
