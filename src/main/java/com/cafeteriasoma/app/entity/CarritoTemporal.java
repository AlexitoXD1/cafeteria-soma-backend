package com.cafeteriasoma.app.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Carrito temporal que soporta usuarios autenticados y anónimos.
 * - Usuario autenticado: id_usuario != null, session_id = null
 * - Usuario anónimo: session_id != null, id_usuario = null
 */
@Entity
@Table(
        name = "carrito_temporal",
        indexes = {
                @Index(name = "idx_carrito_session", columnList = "session_id"),
                @Index(name = "idx_carrito_usuario", columnList = "id_usuario")
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_carrito_usuario_producto",
                        columnNames = {"id_usuario", "id_producto"}
                ),
                @UniqueConstraint(
                        name = "uk_carrito_session_producto",
                        columnNames = {"session_id", "id_producto"}
                )
        }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ToString(exclude = {"usuario", "producto"})
@EqualsAndHashCode(of = "idCarrito")
public class CarritoTemporal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    private Long idCarrito;

    // DEBE SER NULLABLE (sin nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "id_usuario",
            foreignKey = @ForeignKey(name = "fk_carrito_usuario")
    )
    private Usuario usuario;

    // DEBE SER NULLABLE
    @Column(name = "session_id", length = 100)
    private String sessionId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "id_producto",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_carrito_producto")
    )
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    @Builder.Default
    private Integer cantidad = 1;

    @CreationTimestamp
    @Column(name = "fecha_agregado", updatable = false)
    private LocalDateTime fechaAgregado;
}
