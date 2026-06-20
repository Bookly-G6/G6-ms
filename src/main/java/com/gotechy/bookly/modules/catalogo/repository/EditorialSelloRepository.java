package com.gotechy.bookly.modules.catalogo.repository;

import com.gotechy.bookly.modules.catalogo.model.EditorialSello;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditorialSelloRepository
    extends JpaRepository<EditorialSello, Integer>
{
    List<EditorialSello> findByActivaTrue();
}
