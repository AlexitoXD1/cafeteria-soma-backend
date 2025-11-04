package com.cafeteriasoma.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cafeteriasoma.app.entity.Usuario;
import com.cafeteriasoma.app.entity.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    // Busca un código de verificación específico
    Optional<VerificationCode> findByCodigo(String codigo);

    // Busca el código asociado a un usuario
    Optional<VerificationCode> findByUsuario(Usuario usuario);

    // Elimina los códigos anteriores del mismo usuario (opcionalmente antes de crear uno nuevo)
    void deleteByUsuario(Usuario usuario);
}
