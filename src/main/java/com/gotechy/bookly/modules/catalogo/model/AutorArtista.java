package com.gotechy.bookly.modules.catalogo.model;

import jakarta.persistence.*;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "autor_artista")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutorArtista {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "autor_artista_seq")
    @SequenceGenerator(name = "autor_artista_seq", sequenceName = "autor_artista_id_autor_artista_seq", allocationSize = 1)
    @Column(name = "id_autor_artista")
    private Integer idAutorArtista;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "biografia", columnDefinition = "TEXT")
    private String biografia;

    @Column(name = "activa", nullable = false)
    private Boolean activa = true;
}
