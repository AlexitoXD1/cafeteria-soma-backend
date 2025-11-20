package com.cafeteriasoma.app.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Representa una venta o pedido realizado por un usuario.
 * Incluye información sobre el cliente, el total y el método de pago.
 */
@Entity
@Table(name = "venta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"usuario", "detalles"})
@EqualsAndHashCode(of = "idVenta")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Long idVenta;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "id_usuario",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_venta_usuario")
    )
    private Usuario usuario;

    @Column(name = "fecha_venta", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime fechaVenta;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "metodo_pago", nullable = false, length = 30)
    private String metodoPago; // Ejemplo: "Tarjeta", "Efectivo", "Yape", etc.

    @Column(name = "estado", nullable = false, length = 30)
    @Builder.Default
    private String estado = "COMPLETADO"; // O "PENDIENTE", "CANCELADO"

    @Column(name = "voucher_url", length = 255)
    private String voucherUrl; // Ruta al PDF generado

    /** Relación 1:N con DetalleVenta */
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<DetalleVenta> detalles = new HashSet<>();
}
