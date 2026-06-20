package com.gotechy.bookly.modules.catalogo.repository;

import com.gotechy.bookly.modules.catalogo.model.Producto;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, UUID> {
    List<Producto> findByActivoTrue();
}
