package com.gotechy.bookly.modules.ventas.repository;

import com.gotechy.bookly.modules.ventas.model.DetalleVenta;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleVentaRepository
    extends JpaRepository<DetalleVenta, Integer>
{
    List<DetalleVenta> findByIdVenta(UUID idVenta);
}
