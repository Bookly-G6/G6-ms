package com.gotechy.bookly.modules.logistica.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gotechy.bookly.modules.logistica.model.Envio;

public interface EnvioRepository extends JpaRepository<Envio, UUID> {
    List<Envio> findByActivoTrue();

    Optional<Envio> findByIdVentaAndActivoTrue(UUID idVenta);

    List<Envio> findByIdVentaInAndActivoTrue(Collection<UUID> idVentas);
}
