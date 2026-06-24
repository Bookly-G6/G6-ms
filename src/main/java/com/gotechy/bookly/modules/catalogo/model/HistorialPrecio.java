package com.gotechy.bookly.modules.catalogo.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "historial_precio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialPrecio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Integer idHistorialPrecio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Column(
        name = "precio_costo_anterior",
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal precioCostoAnterior;

    @Column(
        name = "precio_venta_anterior",
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal precioVentaAnterior;

    @Column(
        name = "precio_costo_nuevo",
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal precioCostoNuevo;

    @Column(
        name = "precio_venta_nuevo",
        nullable = false,
        precision = 10,
        scale = 2
    )
    private BigDecimal precioVentaNuevo;

    @Column(name = "fecha_cambio", nullable = false)
    private LocalDateTime fechaCambio;

    @Column(name = "id_empleado", nullable = false)
    private UUID idEmpleado;
}
