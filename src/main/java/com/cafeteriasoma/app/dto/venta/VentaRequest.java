package com.cafeteriasoma.app.dto.venta;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VentaRequest {

    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;  // EFECTIVO | TARJETA | YAPE | PLIN
}
