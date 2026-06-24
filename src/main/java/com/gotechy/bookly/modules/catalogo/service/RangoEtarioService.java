package com.gotechy.bookly.modules.catalogo.service;

import com.gotechy.bookly.modules.catalogo.model.RangoEtario;
import com.gotechy.bookly.modules.catalogo.repository.RangoEtarioRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RangoEtarioService {

    private final RangoEtarioRepository rangoEtarioRepository;

    public List<RangoEtario> listarTodos() {
        // Acá usamos findAll() porque no hay soft delete
        return rangoEtarioRepository.findAll();
    }

    public RangoEtario crear(RangoEtario rangoEtario) {
        return rangoEtarioRepository.save(rangoEtario);
    }

    public void eliminar(Integer id) {
        if (!rangoEtarioRepository.existsById(id)) {
            throw new EntityNotFoundException(
                "Rango etario con ID " + id + " no encontrado"
            );
        }
        rangoEtarioRepository.deleteById(id);
    }
}
