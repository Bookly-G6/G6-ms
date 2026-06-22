package com.gotechy.bookly.modules.ventas.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gotechy.bookly.modules.ventas.model.CarritoItem;

public interface CarritoItemRepository extends JpaRepository<CarritoItem, UUID> {
    List<CarritoItem> findByIdCarrito(UUID idCarrito);
    Optional<CarritoItem> findByIdItemAndIdCarrito(UUID idItem, UUID idCarrito);
    void deleteByIdCarrito(UUID idCarrito);
}
