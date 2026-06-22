package com.gotechy.bookly.modules.ventas.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gotechy.bookly.modules.ventas.model.VentaPago;

public interface VentaPagoRepository extends JpaRepository<VentaPago, Integer> {
    List<VentaPago> findByIdVenta(UUID idVenta);
}
