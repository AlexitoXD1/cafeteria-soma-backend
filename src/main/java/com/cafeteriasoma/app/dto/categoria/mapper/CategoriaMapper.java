package com.cafeteriasoma.app.dto.categoria.mapper;

import com.cafeteriasoma.app.dto.categoria.response.CategoriaResponse;
import com.cafeteriasoma.app.entity.Categoria;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CategoriaMapper {

    public static Categoria toEntity(String nombre, String descripcion) {
        return Categoria.builder()
                .nombre(nombre)
                .descripcion(descripcion)
                .activo(true)
                .build();
    }

    public static void updateEntity(Categoria categoria, String nombre, String descripcion) {
        categoria.setNombre(nombre);
        categoria.setDescripcion(descripcion);
    }

    public static CategoriaResponse toResponse(Categoria categoria) {
        return CategoriaResponse.builder()
                .idCategoria(categoria.getIdCategoria())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .activo(categoria.getActivo())
                .fechaCreacion(categoria.getFechaCreacion())
                .fechaActualizacion(categoria.getFechaActualizacion())
                .build();
    }
}
