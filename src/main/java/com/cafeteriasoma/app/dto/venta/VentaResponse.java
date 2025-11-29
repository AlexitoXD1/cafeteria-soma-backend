package com.cafeteriasoma.app.dto.venta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VentaResponse {

    private Long idVenta;
    private Long idUsuario;
    private BigDecimal total;
    private String metodoPago;
    private LocalDateTime fechaVenta;

    private List<DetalleVentaResponse> detalles;
}
