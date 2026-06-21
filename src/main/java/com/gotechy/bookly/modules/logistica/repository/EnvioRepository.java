package com.gotechy.bookly.modules.logistica.repository;

import com.gotechy.bookly.modules.logistica.model.Envio;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvioRepository extends JpaRepository<Envio, UUID> {
    // Para listar los envíos activos en el panel de control
    List<Envio> findByActivoTrue();

    Optional<Envio> findByIdVentaAndActivoTrue(UUID idVenta);
}
