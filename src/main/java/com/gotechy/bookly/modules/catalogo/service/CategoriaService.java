package com.gotechy.bookly.modules.catalogo.service;

import com.gotechy.bookly.modules.catalogo.model.Categoria;
import com.gotechy.bookly.modules.catalogo.repository.CategoriaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    // Solo traemos las activas para el frontend
    public List<Categoria> listarCategoriasActivas() {
        return categoriaRepository.findByActivaTrue();
    }

    public Categoria crearCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }
}
