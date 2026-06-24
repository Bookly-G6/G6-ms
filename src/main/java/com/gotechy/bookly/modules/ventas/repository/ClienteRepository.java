package com.gotechy.bookly.modules.ventas.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gotechy.bookly.modules.ventas.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    @Query("SELECT c FROM Cliente c WHERE c.persona.idPersona = :idPersona")
    Optional<Cliente> findByIdPersona(@Param("idPersona") UUID idPersona);
}
