package com.cafeteriasoma.app.dto.venta;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetalleVentaResponse {

    private Long idDetalle;

    private Long idProducto;
    private String nombreProducto;

    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
