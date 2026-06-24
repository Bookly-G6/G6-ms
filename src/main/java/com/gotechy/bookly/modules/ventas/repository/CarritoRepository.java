package com.gotechy.bookly.modules.ventas.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gotechy.bookly.modules.ventas.model.Carrito;

public interface CarritoRepository extends JpaRepository<Carrito, UUID> {
    Optional<Carrito> findByIdClienteAndActivoTrue(UUID idCliente);
}
