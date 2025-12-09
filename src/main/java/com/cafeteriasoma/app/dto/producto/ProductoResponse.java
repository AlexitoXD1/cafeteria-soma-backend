package com.cafeteriasoma.app.dto.producto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa la información de producto devuelta al frontend.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoResponse {
    
    private Long idProducto;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;
    private String imagenUrl;
    private Long idCategoria;
    private String categoriaNombre;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    /** Precio final después de aplicar la promoción (si existe). */
    private BigDecimal precioConDescuento;

    /** Indica si el producto está bajo alguna promoción aplicada. */
    private Boolean tienePromocion;

    /** Porcentaje de descuento aplicado (ej. 15 para 15%). */
    private BigDecimal porcentajeDescuento;

}
