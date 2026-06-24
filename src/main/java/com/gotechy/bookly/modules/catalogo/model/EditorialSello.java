package com.gotechy.bookly.modules.catalogo.model;

import jakarta.persistence.*;
import jakarta.persistence.SequenceGenerator;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "editorial_sello_seq")
    @SequenceGenerator(name = "editorial_sello_seq", sequenceName = "editorial_sello_id_editorial_sello_seq", allocationSize = 1)
    @Column(name = "id_editorial_sello")
    private Integer idEditorialSello;

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombreEditorial;

    @Column(name = "activa", nullable = false)
    private Boolean activa = true;
}
