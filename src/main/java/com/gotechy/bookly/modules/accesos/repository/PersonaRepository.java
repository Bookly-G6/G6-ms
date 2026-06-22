package com.gotechy.bookly.modules.accesos.repository;

import com.gotechy.bookly.modules.accesos.model.Persona;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, UUID> {
}
