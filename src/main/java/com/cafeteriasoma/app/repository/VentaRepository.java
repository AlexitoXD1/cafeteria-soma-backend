package com.cafeteriasoma.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cafeteriasoma.app.entity.Usuario;
import com.cafeteriasoma.app.entity.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query("SELECT v FROM Venta v WHERE v.usuario = :usuario")
    List<Venta> findByUsuario(@Param("usuario") Usuario usuario);
}
