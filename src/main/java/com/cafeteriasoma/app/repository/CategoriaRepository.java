package com.cafeteriasoma.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cafeteriasoma.app.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {}