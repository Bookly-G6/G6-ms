package com.gotechy.bookly.modules.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gotechy.bookly.modules.ventas.model.FormaPagoCatalog;

public interface FormaPagoCatalogRepository extends JpaRepository<FormaPagoCatalog, Integer> {
}
