package com.gotechy.bookly.modules.catalogo.service;

import com.gotechy.bookly.modules.catalogo.model.TipoProducto;
import com.gotechy.bookly.modules.catalogo.repository.TipoProductoRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TipoProductoService {

    private final TipoProductoRepository tipoProductoRepository;

    public List<TipoProducto> listarActivos() {
        return tipoProductoRepository.findByActivaTrue();
    }

    public TipoProducto crear(TipoProducto tipoProducto) {
        return tipoProductoRepository.save(tipoProducto);
    }

    public void eliminar(Integer id) {
        TipoProducto tipo = tipoProductoRepository
            .findById(id)
            .orElseThrow(() ->
                new EntityNotFoundException(
                    "Tipo de producto con ID " + id + " no encontrado"
                )
            );
        tipo.setActiva(false);
        tipoProductoRepository.save(tipo);
    }
}
