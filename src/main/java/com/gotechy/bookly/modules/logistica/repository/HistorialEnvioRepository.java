package com.gotechy.bookly.modules.logistica.repository;

import com.gotechy.bookly.modules.logistica.model.HistorialEnvio;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistorialEnvioRepository
    extends JpaRepository<HistorialEnvio, Integer>
{
    // Devuelve la línea de tiempo completa de un paquete, desde hoy hacia atrás
    List<HistorialEnvio> findByEnvio_IdEnvioOrderByFechaCambioDesc(
        UUID idEnvio
    );
}
