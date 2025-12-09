package com.cafeteriasoma.app.dto.promocion;

import com.cafeteriasoma.app.dto.producto.ProductoResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PromocionResponse {
    private Long idPromocion;
    private String nombre;
    private String descripcion;
    private BigDecimal porcentajeDescuento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Boolean activo;
    
    // Devolvemos la lista de productos asociados para que el front sepa a qué aplica
    private List<ProductoResponse> productos;
    
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}