package com.gotechy.bookly.modules.ventas.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gotechy.bookly.modules.ventas.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, UUID> {
    List<Venta> findByIdClienteOrderByFechaDesc(UUID idCliente);
}
