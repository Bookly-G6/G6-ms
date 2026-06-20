package com.gotechy.bookly.modules.catalogo.repository;

import com.gotechy.bookly.modules.catalogo.model.AutorArtista;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorArtistaRepository
    extends JpaRepository<AutorArtista, Integer>
{
    List<AutorArtista> findByActivaTrue();
}
