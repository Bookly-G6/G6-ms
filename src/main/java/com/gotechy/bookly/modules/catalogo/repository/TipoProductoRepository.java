package com.gotechy.bookly.modules.catalogo.repository;

import com.gotechy.bookly.modules.catalogo.model.TipoProducto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoProductoRepository
    extends JpaRepository<TipoProducto, Integer>
{
    List<TipoProducto> findByActivaTrue();
}
