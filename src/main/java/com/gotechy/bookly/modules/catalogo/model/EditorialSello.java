package com.gotechy.bookly.modules.catalogo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "editorial_sello")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditorialSello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_editorial_sello")
    private Integer idEditorialSello;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombreEditorial;

    @Column(name = "activa", nullable = false)
    private Boolean activa = true;
}
