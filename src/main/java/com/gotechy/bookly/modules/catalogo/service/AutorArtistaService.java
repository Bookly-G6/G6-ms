package com.gotechy.bookly.modules.catalogo.service;

import com.gotechy.bookly.modules.catalogo.model.AutorArtista;
import com.gotechy.bookly.modules.catalogo.repository.AutorArtistaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutorArtistaService {

    private final AutorArtistaRepository autorArtistaRepository;

    public List<AutorArtista> listarActivos() {
        return autorArtistaRepository.findByActivaTrue();
    }

    public AutorArtista crear(AutorArtista autorArtista) {
        // Por defecto, la entidad ya nace con activa = true
        return autorArtistaRepository.save(autorArtista);
    }

    public void eliminar(Integer id) {
        AutorArtista autor = autorArtistaRepository
            .findById(id)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "Autor o Artista con ID " + id + " no encontrado"
                )
            );

        autor.setActiva(false);
        autorArtistaRepository.save(autor);
    }
}
