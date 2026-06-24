package com.gotechy.bookly.modules.ventas.repository;

import com.gotechy.bookly.modules.ventas.model.Empleado;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, UUID> {
    Optional<Empleado> findFirstByOrderByIdEmpleadoAsc();
    Optional<Empleado> findByLegajo(String legajo);
    Optional<Empleado> findByIdPersona(UUID idPersona);
}
