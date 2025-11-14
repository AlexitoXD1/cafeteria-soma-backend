package com.cafeteriasoma.app.dto.producto;

import com.cafeteriasoma.app.entity.Categoria;
import com.cafeteriasoma.app.entity.Producto;

public class ProductoMapper {

    /**
     * Convierte un DTO de entrada en una entidad Producto.
     */
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

    /**
     * Convierte una entidad Producto en un DTO de salida.
     */
    public static ProductoResponse toResponse(Producto producto) {
        return ProductoResponse.builder()
                .idProducto(producto.getIdProducto())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .imagenUrl(producto.getImagenUrl())
                .categoriaNombre(producto.getCategoria().getNombre())
                .activo(producto.getActivo())
                .fechaCreacion(producto.getFechaCreacion())
                .fechaActualizacion(producto.getFechaActualizacion())
                .build();
    }
}
