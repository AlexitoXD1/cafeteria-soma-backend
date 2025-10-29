package com.cafeteriasoma.app.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Representa un token JWT generado para un usuario específico.
 * Permite validar o invalidar el acceso sin necesidad de eliminar el token globalmente.
 */
@Entity
@Table(name = "token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "usuario")
@EqualsAndHashCode(of = "id")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_token")
    private Long id;

    @Column(name = "token", unique = true, nullable = false, length = 512)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type", nullable = false, length = 20)
    private TokenType tokenType = TokenType.BEARER; // BEARER = estándar en JWT

    @Column(name = "revoked", nullable = false)
    private boolean revoked; // true = el usuario cerró sesión

    @Column(name = "expired", nullable = false)
    private boolean expired; // true = el token ya no es válido

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_usuario",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_token_usuario")
    )
    private Usuario usuario;
}
