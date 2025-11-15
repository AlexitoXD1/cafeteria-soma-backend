package com.cafeteriasoma.app.dto.carrito;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarritoItemResponse {

    private Long idCarrito;

    private Long idProducto;
    private String nombreProducto;
    private String imagenUrl;

    private BigDecimal precioUnitario;
    private Integer cantidad;
    private BigDecimal subtotal;

    private LocalDateTime fechaAgregado;
}
