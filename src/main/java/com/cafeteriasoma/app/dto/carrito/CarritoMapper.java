package com.cafeteriasoma.app.dto.carrito;

import java.math.BigDecimal;

import com.cafeteriasoma.app.entity.CarritoTemporal;
import com.cafeteriasoma.app.entity.Producto;

public class CarritoMapper {

    public static CarritoItemResponse toResponse(CarritoTemporal carrito) {
        Producto producto = carrito.getProducto();

        BigDecimal subtotal = producto.getPrecio()
                .multiply(BigDecimal.valueOf(carrito.getCantidad()));

        return CarritoItemResponse.builder()
                .idCarrito(carrito.getIdCarrito())
                .idProducto(producto.getIdProducto())
                .nombreProducto(producto.getNombre())
                .imagenUrl(producto.getImagenUrl())
                .precioUnitario(producto.getPrecio())
                .cantidad(carrito.getCantidad())
                .subtotal(subtotal)
                .fechaAgregado(carrito.getFechaAgregado())
                .build();
    }
}
