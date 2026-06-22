package com.gotechy.bookly.modules.ventas.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gotechy.bookly.modules.ventas.model.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, UUID> {
    Optional<Empleado> findFirstByOrderByIdEmpleadoAsc();
}
