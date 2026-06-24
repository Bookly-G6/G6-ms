package com.gotechy.bookly.modules.logistica.model;

import com.gotechy.bookly.core.enums.EstadoLogistica;
import com.gotechy.bookly.core.enums.TipoEnvio;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "envio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_envio", updatable = false, nullable = false)
    private UUID idEnvio;

    @Column(name = "id_venta", nullable = false, unique = true)
    private UUID idVenta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_envio", nullable = false, length = 50)
    private TipoEnvio tipoEnvio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_logistica", nullable = false, length = 50)
    private EstadoLogistica estadoLogistica;

    @Column(name = "codigo_retiro", length = 50)
    private String codigoRetiro;

    @Column(name = "empresa_correo", length = 100)
    private String empresaCorreo;

    @Column(name = "numero_tracking", length = 100)
    private String numeroTracking;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @CreationTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}
