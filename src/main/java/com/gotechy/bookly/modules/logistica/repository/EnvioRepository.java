package com.gotechy.bookly.modules.logistica.repository;

import com.gotechy.bookly.core.enums.EstadoLogistica; // <-- Importante
import com.gotechy.bookly.modules.logistica.model.Envio;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EnvioRepository extends JpaRepository<Envio, UUID> {
    List<Envio> findByActivoTrue();

    Optional<Envio> findByIdVentaAndActivoTrue(UUID idVenta);

    List<Envio> findByIdVentaInAndActivoTrue(Collection<UUID> idVentas);

    @Query(
        "SELECT e FROM Envio e JOIN Venta v ON e.idVenta = v.idVenta " +
            "JOIN Cliente c ON v.idCliente = c.idCliente " +
            "JOIN c.persona p " +
            "WHERE e.estadoLogistica = :estado " +
            "AND (:idVenta IS NULL OR e.idVenta = :idVenta) " +
            "AND (:terminoBusqueda IS NULL OR p.nombre ILIKE %:terminoBusqueda% " +
            "OR p.apellido ILIKE %:terminoBusqueda% OR p.dni LIKE %:terminoBusqueda%)"
    )
    Page<Envio> buscarEnviosDinamicos(
        @Param("estado") EstadoLogistica estado, // <-- Cambio: Tipo Enum
        @Param("idVenta") UUID idVenta,
        @Param("terminoBusqueda") String terminoBusqueda,
        Pageable pageable
    );
}
