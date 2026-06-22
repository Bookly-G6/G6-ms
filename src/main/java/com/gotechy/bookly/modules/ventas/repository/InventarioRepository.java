package com.gotechy.bookly.modules.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gotechy.bookly.modules.ventas.model.Inventario;
import com.gotechy.bookly.modules.ventas.model.InventarioId;

public interface InventarioRepository extends JpaRepository<Inventario, InventarioId> {
}
