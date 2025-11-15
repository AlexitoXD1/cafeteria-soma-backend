package com.cafeteriasoma.app.dto.producto;

import com.cafeteriasoma.app.entity.Categoria;
import com.cafeteriasoma.app.entity.Producto;

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
        return ProductoResponse.builder()
                .idProducto(producto.getIdProducto())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .imagenUrl(producto.getImagenUrl())
                .idCategoria(producto.getCategoria().getIdCategoria())
                .categoriaNombre(producto.getCategoria().getNombre())
                .activo(producto.getActivo())
                .fechaCreacion(producto.getFechaCreacion())
                .fechaActualizacion(producto.getFechaActualizacion())
                .build();
    }
}
