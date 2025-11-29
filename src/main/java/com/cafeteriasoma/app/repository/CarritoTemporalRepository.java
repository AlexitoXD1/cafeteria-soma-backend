package com.cafeteriasoma.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cafeteriasoma.app.entity.CarritoTemporal;
import com.cafeteriasoma.app.entity.Producto;
import com.cafeteriasoma.app.entity.Usuario;

@Repository
public interface CarritoTemporalRepository extends JpaRepository<CarritoTemporal, Long> {

    @Query("SELECT ct FROM CarritoTemporal ct WHERE ct.usuario = :usuario")
    List<CarritoTemporal> findByUsuario(@Param("usuario") Usuario usuario);

    @Query("SELECT ct FROM CarritoTemporal ct WHERE ct.usuario = :usuario AND ct.producto = :producto")
    Optional<CarritoTemporal> findByUsuarioAndProducto(
            @Param("usuario") Usuario usuario, 
            @Param("producto") Producto producto
    );

    @Query("DELETE FROM CarritoTemporal ct WHERE ct.usuario = :usuario")
    void deleteByUsuario(@Param("usuario") Usuario usuario);
}
