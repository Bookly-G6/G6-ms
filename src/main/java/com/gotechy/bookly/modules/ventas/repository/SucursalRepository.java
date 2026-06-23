package com.gotechy.bookly.modules.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gotechy.bookly.modules.ventas.model.Sucursal;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {
}
