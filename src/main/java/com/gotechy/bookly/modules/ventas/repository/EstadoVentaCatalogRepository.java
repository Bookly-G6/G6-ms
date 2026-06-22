package com.gotechy.bookly.modules.ventas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gotechy.bookly.modules.ventas.model.EstadoVentaCatalog;

public interface EstadoVentaCatalogRepository extends JpaRepository<EstadoVentaCatalog, Integer> {
    Optional<EstadoVentaCatalog> findByNombreEstadoIgnoreCase(String nombreEstado);
}
