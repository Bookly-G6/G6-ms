package com.gotechy.bookly.modules.catalogo.repository;

import com.gotechy.bookly.modules.catalogo.model.HistorialPrecio;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialPrecioRepository
    extends JpaRepository<HistorialPrecio, Integer>
{
    List<HistorialPrecio> findByProductoIdProductoOrderByFechaCambioDesc(
        UUID idProducto
    );
}
