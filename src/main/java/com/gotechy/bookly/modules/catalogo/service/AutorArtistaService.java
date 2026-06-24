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

    public AutorArtista obtenerPorId(Integer id) {
        AutorArtista autor = autorArtistaRepository
            .findById(id)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "Autor o Artista con ID " + id + " no encontrado"
                )
            );

        if (!autor.getActiva()) {
            throw new IllegalStateException(
                "El autor/artista se encuentra inactivo"
            );
        }
        return autor;
    }

    public AutorArtista crear(AutorArtista autorArtista) {
        autorArtista.setActiva(true);
        return autorArtistaRepository.save(autorArtista);
    }

    public AutorArtista actualizar(Integer id, AutorArtista autorActualizado) {
        AutorArtista autor = obtenerPorId(id);
        autor.setNombre(autorActualizado.getNombre());
        return autorArtistaRepository.save(autor);
    }

    public void eliminar(Integer id) {
        AutorArtista autor = autorArtistaRepository
            .findById(id)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "Autor o Artista con ID " + id + " no encontrado"
                )
            );

        if (!autor.getActiva()) {
            throw new IllegalStateException(
                "El autor/artista ya se encuentra inactivo."
            );
        }

        autor.setActiva(false);
        autorArtistaRepository.save(autor);
    }
}
