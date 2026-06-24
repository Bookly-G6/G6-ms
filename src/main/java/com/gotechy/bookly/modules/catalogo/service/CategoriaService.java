package com.gotechy.bookly.modules.catalogo.service;

import com.gotechy.bookly.modules.catalogo.model.Categoria;
import com.gotechy.bookly.modules.catalogo.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<Categoria> listarCategoriasActivas() {
        return categoriaRepository.findByActivaTrue();
    }

    public Categoria obtenerPorId(Integer id) {
        Categoria categoria = categoriaRepository
            .findById(id)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "Categoría con ID " + id + " no encontrada"
                )
            );

        if (!categoria.getActiva()) {
            throw new IllegalStateException(
                "La categoría se encuentra inactiva"
            );
        }
        return categoria;
    }

    public Categoria crearCategoria(Categoria categoria) {
        categoria.setActiva(true);
        return categoriaRepository.save(categoria);
    }

    public void eliminarCategoria(Integer id) {
        Categoria categoria = categoriaRepository
            .findById(id)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "Categoría con ID " + id + " no encontrada"
                )
            );

        if (!categoria.getActiva()) {
            throw new IllegalStateException(
                "La categoría ya se encuentra inactiva."
            );
        }

        categoria.setActiva(false);
        categoriaRepository.save(categoria);
    }
}
