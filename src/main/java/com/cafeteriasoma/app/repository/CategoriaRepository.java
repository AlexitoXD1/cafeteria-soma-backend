package com.cafeteriasoma.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cafeteriasoma.app.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    List<Categoria> findByActivoTrue();

    @Query("SELECT c FROM Categoria c WHERE c.idCategoria = :id AND c.activo = true")
    Optional<Categoria> findByIdAndActivoTrue(@Param("id") Long idCategoria);

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdCategoriaNot(String nombre, Long idCategoria);
}
