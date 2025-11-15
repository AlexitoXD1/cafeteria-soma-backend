package com.cafeteriasoma.app.dto.venta;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.cafeteriasoma.app.entity.DetalleVenta;
import com.cafeteriasoma.app.entity.Venta;

public class VentaMapper {

    public static DetalleVentaResponse toDetalleResponse(DetalleVenta detalle) {
        BigDecimal subtotal = detalle.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(detalle.getCantidad()));

        return DetalleVentaResponse.builder()
                .idDetalle(detalle.getIdDetalle())
                .idProducto(detalle.getProducto().getIdProducto())
                .nombreProducto(detalle.getProducto().getNombre())
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .subtotal(subtotal)
                .build();
    }

    public static VentaResponse toVentaResponse(Venta venta, List<DetalleVenta> detalles) {

        List<DetalleVentaResponse> detalleResponses = detalles.stream()
                .map(VentaMapper::toDetalleResponse)
                .collect(Collectors.toList());

        return VentaResponse.builder()
                .idVenta(venta.getIdVenta())
                .idUsuario(venta.getUsuario().getIdUsuario())
                .total(venta.getTotal())
                .metodoPago(venta.getMetodoPago())
                .fechaVenta(venta.getFechaVenta())
                .detalles(detalleResponses)
                .build();
    }
}
