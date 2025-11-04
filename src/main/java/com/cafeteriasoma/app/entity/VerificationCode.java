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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Código de verificación para confirmar el registro de un usuario.
 * Asociado a la entidad Usuario mediante una relación OneToOne.
 */
@Entity
@Table(
    name = "codigo_verificacion",
    indexes = {
        @Index(name = "idx_codigo_valor", columnList = "codigo")
    }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@ToString(exclude = "usuario")
@EqualsAndHashCode(of = "id")
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_codigo")
    private Long id;

    @Column(name = "codigo", nullable = false, length = 6)
    private String codigo;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(name = "usado", nullable = false)
    @Builder.Default
    private Boolean usado = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_usuario",
        nullable = false,
        unique = true,
        foreignKey = @ForeignKey(name = "fk_codigo_usuario")
    )
    private Usuario usuario;

    /**
     * Verifica si el código aún es válido.
     */
    public boolean esValido() {
        return !usado && LocalDateTime.now().isBefore(fechaExpiracion);
    }
}
