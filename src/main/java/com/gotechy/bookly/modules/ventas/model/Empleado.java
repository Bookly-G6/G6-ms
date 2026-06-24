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

@Table(name = "empleado")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empleado {

    @Id
    @Column(name = "id_empleado")
    private UUID idEmpleado;

    @Column(name = "id_persona", nullable = false)
    private UUID idPersona;

    @Column(name = "legajo")
    private String legajo;

    @Column(name = "cargo")
    private String cargo;

}
