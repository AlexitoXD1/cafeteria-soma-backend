package com.cafeteriasoma.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cafeteriasoma.app.entity.Token;
import com.cafeteriasoma.app.entity.Usuario;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("""
        SELECT t FROM Token t 
        WHERE t.usuario = :usuario 
        AND (t.expired = false OR t.revoked = false)
    """)
    List<Token> findAllValidTokensByUsuario(Usuario usuario);

    Optional<Token> findByToken(String token);
}
