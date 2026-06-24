package com.gotechy.bookly.modules.ventas.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@Table(name = "venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_venta", updatable = false, nullable = false)
    private UUID idVenta;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "subtotal_sin_descuentos", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotalSinDescuentos;

    @Column(name = "total_final", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalFinal;

    @Column(name = "origen_venta", nullable = false, length = 50)
    private String origenVenta;

    @Column(name = "id_estado_venta", nullable = false)
    private Integer idEstadoVenta;

    @Column(name = "id_sucursal", nullable = false)
    private Integer idSucursal;

    @Column(name = "id_cliente")
    private UUID idCliente;

    @Column(name = "id_empleado")
    private UUID idEmpleado;
}
