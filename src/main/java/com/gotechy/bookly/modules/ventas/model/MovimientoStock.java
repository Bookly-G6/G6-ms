package com.gotechy.bookly.modules.ventas.model;

import java.time.LocalDateTime;
import java.util.UUID;

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
@Table(name = "movimiento_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoStock {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movimiento_stock_seq")
    @SequenceGenerator(name = "movimiento_stock_seq", sequenceName = "movimiento_stock_id_movimiento_seq", allocationSize = 1)
    @Column(name = "id_movimiento")
    private Integer idMovimiento;

    @Column(name = "id_sucursal", nullable = false)
    private Integer idSucursal;

    @Column(name = "id_producto", nullable = false)
    private UUID idProducto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "tipo_movimiento", nullable = false, length = 100)
    private String tipoMovimiento;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "id_empleado", nullable = false)
    private UUID idEmpleado;

    @Column(name = "stock_resultante")
    private Integer stockResultante;
}
