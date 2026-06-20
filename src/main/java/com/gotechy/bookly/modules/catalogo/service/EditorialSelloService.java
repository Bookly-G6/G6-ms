package com.gotechy.bookly.modules.catalogo.service;

import com.gotechy.bookly.modules.catalogo.model.EditorialSello;
import com.gotechy.bookly.modules.catalogo.repository.EditorialSelloRepository;
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

    public EditorialSello crearEditorialSello(EditorialSello editorialSello) {
        return editorialSelloRepository.save(editorialSello);
    }
}
