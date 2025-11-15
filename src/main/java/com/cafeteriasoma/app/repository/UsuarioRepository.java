package com.cafeteriasoma.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cafeteriasoma.app.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    @Query("SELECT u FROM Usuario u WHERE u.correo = :correo")
    Optional<Usuario> findByCorreo(@Param("correo") String correo);
}
