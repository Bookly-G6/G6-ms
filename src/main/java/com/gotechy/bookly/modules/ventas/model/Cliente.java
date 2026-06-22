package com.gotechy.bookly.modules.ventas.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @Column(name = "id_cliente")
    private UUID idCliente;

    @Column(name = "id_persona", nullable = false)
    private UUID idPersona;

    @Column(name = "puntos_fidelidad", nullable = false)
    private Integer puntosFidelidad;
}
