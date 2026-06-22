package com.gotechy.bookly.modules.ventas.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gotechy.bookly.modules.ventas.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    Optional<Cliente> findByIdPersona(UUID idPersona);
}
