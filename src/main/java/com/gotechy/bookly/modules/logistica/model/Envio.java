package com.gotechy.bookly.modules.logistica.model;

import com.gotechy.bookly.core.enums.EstadoLogistica;
import com.gotechy.bookly.core.enums.TipoEnvio;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
@Entity
@Table(name = "envio")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idEnvio;

    @Column(nullable = false)
    private UUID idVenta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEnvio tipoEnvio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoLogistica estadoLogistica;

    // Estos campos son nulos por defecto, se llenan con el Envio
    @Column(length = 100)
    private String empresaCorreo;

    @Column(length = 50)
    private String numeroTracking;

    @Column(length = 10)
    private String codigoRetiro; // Ej: "BKL-9823" para que el cliente retire

    private LocalDateTime fechaActualizacion;

    @Column(length = 255)
    private String observaciones;

    @Column(nullable = false)
    private Boolean activo = true;
}
