package com.gotechy.bookly.modules.accesos.repository;

import com.gotechy.bookly.modules.accesos.model.Rol;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByNombreRol(String nombreRol);
}
