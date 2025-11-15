package com.cafeteriasoma.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cafeteriasoma.app.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByActivoTrue();

    List<Producto> findByCategoria_IdCategoriaAndActivoTrue(Long idCategoria);

    @Query("SELECT p FROM Producto p WHERE p.idProducto = :id AND p.activo = true")
    Optional<Producto> findByIdAndActivoTrue(@Param("id") Long idProducto);

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdProductoNot(String nombre, Long idProducto);
}
