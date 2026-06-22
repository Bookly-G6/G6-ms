package com.gotechy.bookly.modules.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gotechy.bookly.modules.ventas.model.MovimientoStock;

public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, Integer> {
}
