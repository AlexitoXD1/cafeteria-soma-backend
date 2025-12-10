package com.cafeteriasoma.app.dto.producto;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.cafeteriasoma.app.entity.Categoria;
import com.cafeteriasoma.app.entity.Producto;
import com.cafeteriasoma.app.entity.Promocion;

public class ProductoMapper {

    public static Producto toEntity(ProductoRequest dto, Categoria categoria) {
        return Producto.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .precio(dto.getPrecio())
                .stock(dto.getStock())
                .imagenUrl(dto.getImagenUrl())
                .categoria(categoria)
                .activo(true)
                .build();
    }

    public static void updateEntity(Producto producto, ProductoRequest dto, Categoria categoria) {
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setCategoria(categoria);
    }

    public static ProductoResponse toResponse(Producto producto) {
        return toResponse(producto, null);
    }

    public static ProductoResponse toResponse(Producto producto, Promocion promoActiva) {
        if (producto == null) return null;

        BigDecimal precioOriginal = producto.getPrecio();
        BigDecimal precioFinal = precioOriginal;
        boolean tienePromo = false;
        BigDecimal porcentaje = null;

        if (promoActiva != null
                && Boolean.TRUE.equals(promoActiva.getActivo())
                && promoActiva.getProductos() != null
                && promoActiva.getProductos().contains(producto)
                && promoActiva.getPorcentajeDescuento() != null) {

            porcentaje = promoActiva.getPorcentajeDescuento();
            BigDecimal descuento = precioOriginal
                    .multiply(porcentaje)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            precioFinal = precioOriginal.subtract(descuento);
            if (precioFinal.compareTo(BigDecimal.ZERO) < 0) {
                precioFinal = BigDecimal.ZERO;
            }
            tienePromo = true;
        }

        return ProductoResponse.builder()
                .idProducto(producto.getIdProducto())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(precioOriginal)            // precio original
                .precioConDescuento(precioFinal)   // precio final
                .tienePromocion(tienePromo)
                .porcentajeDescuento(porcentaje)
                .stock(producto.getStock())
                .imagenUrl(producto.getImagenUrl())
                .activo(producto.getActivo())
                // Ajusta según tu DTO de categoría; aquí sólo se ilustra el nombre
                .categoriaNombre(producto.getCategoria() != null ? producto.getCategoria().getNombre() : null)
                .fechaCreacion(producto.getFechaCreacion())
                .fechaActualizacion(producto.getFechaActualizacion())
                .build();
    }
}
