package com.gotechy.bookly.modules.ventas.model;

import java.util.UUID;

import com.gotechy.bookly.modules.accesos.model.Persona;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
    @Column(name = "id_cliente", updatable = false, nullable = false)
    private UUID idCliente;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona", referencedColumnName = "id_persona", nullable = false, unique = true)
    private Persona persona;

    @Column(name = "puntos_fidelidad", nullable = false)
    private Integer puntosFidelidad = 0;
}
