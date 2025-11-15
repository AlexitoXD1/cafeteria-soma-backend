package com.cafeteriasoma.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cafeteriasoma.app.entity.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {
    
    @Query("SELECT r FROM Rol r WHERE r.nombre = :nombre")
    Optional<Rol> findByNombre(@Param("nombre") String nombre);
}