package com.cafeteriasoma.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cafeteriasoma.app.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findAllByOrderByNombreAsc();

    List<Categoria> findAllByActivoTrueOrderByNombreAsc();

    Optional<Categoria> findByIdAndActivoTrue(Long idCategoria);

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdCategoriaNot(String nombre, Long idCategoria);
}
