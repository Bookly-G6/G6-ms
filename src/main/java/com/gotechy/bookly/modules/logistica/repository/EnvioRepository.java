package com.gotechy.bookly.modules.logistica.repository;

import com.gotechy.bookly.modules.logistica.model.Envio;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, UUID> {}
