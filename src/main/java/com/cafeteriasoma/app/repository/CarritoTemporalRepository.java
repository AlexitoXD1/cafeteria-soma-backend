package com.cafeteriasoma.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cafeteriasoma.app.entity.CarritoTemporal;
import com.cafeteriasoma.app.entity.Producto;
import com.cafeteriasoma.app.entity.Usuario;

@Repository
public interface CarritoTemporalRepository extends JpaRepository<CarritoTemporal, Long> {

    // Para usuarios autenticados
    @Query("SELECT ct FROM CarritoTemporal ct WHERE ct.usuario = :usuario")
    List<CarritoTemporal> findByUsuario(@Param("usuario") Usuario usuario);

    @Query("SELECT ct FROM CarritoTemporal ct WHERE ct.usuario = :usuario AND ct.producto = :producto")
    Optional<CarritoTemporal> findByUsuarioAndProducto(
            @Param("usuario") Usuario usuario,
            @Param("producto") Producto producto
    );

    @Modifying
    @Query("DELETE FROM CarritoTemporal ct WHERE ct.usuario = :usuario")
    void deleteByUsuario(@Param("usuario") Usuario usuario);

    // Para usuarios anónimos (por sessionId)
    @Query("SELECT ct FROM CarritoTemporal ct WHERE ct.sessionId = :sessionId")
    List<CarritoTemporal> findBySessionId(@Param("sessionId") String sessionId);

    @Query("SELECT ct FROM CarritoTemporal ct WHERE ct.sessionId = :sessionId AND ct.producto = :producto")
    Optional<CarritoTemporal> findBySessionIdAndProducto(
            @Param("sessionId") String sessionId,
            @Param("producto") Producto producto
    );

    @Modifying
    @Query("DELETE FROM CarritoTemporal ct WHERE ct.sessionId = :sessionId")
    void deleteBySessionId(@Param("sessionId") String sessionId);

    // Migrar carrito anónimo a usuario autenticado
    @Modifying
    @Query("UPDATE CarritoTemporal ct SET ct.usuario = :usuario, ct.sessionId = null WHERE ct.sessionId = :sessionId")
    void migrarCarritoAnonimo(@Param("sessionId") String sessionId, @Param("usuario") Usuario usuario);
}
