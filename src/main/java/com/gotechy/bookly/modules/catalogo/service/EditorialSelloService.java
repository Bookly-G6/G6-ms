package com.gotechy.bookly.modules.catalogo.service;

import com.gotechy.bookly.modules.catalogo.model.EditorialSello;
import com.gotechy.bookly.modules.catalogo.repository.EditorialSelloRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EditorialSelloService {

    private final EditorialSelloRepository editorialSelloRepository;

    public List<EditorialSello> listarEditorialesSelloActivas() {
        return editorialSelloRepository.findByActivaTrue();
    }

    public EditorialSello obtenerPorId(Integer id) {
        EditorialSello editorial = editorialSelloRepository
            .findById(id)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "Editorial con ID " + id + " no encontrada"
                )
            );

        if (!editorial.getActiva()) {
            throw new IllegalStateException(
                "La editorial se encuentra inactiva"
            );
        }
        return editorial;
    }

    public EditorialSello crearEditorialSello(EditorialSello editorialSello) {
        editorialSello.setActiva(true);
        return editorialSelloRepository.save(editorialSello);
    }

    public void eliminarEditorialSello(Integer id) {
        EditorialSello editorial = editorialSelloRepository
            .findById(id)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "Editorial con ID " + id + " no encontrada"
                )
            );

        if (!editorial.getActiva()) {
            throw new IllegalStateException(
                "La editorial ya se encuentra inactiva."
            );
        }

        editorial.setActiva(false);
        editorialSelloRepository.save(editorial);
    }
}
