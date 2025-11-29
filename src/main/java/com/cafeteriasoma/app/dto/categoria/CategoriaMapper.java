package com.cafeteriasoma.app.dto.categoria;

import com.cafeteriasoma.app.entity.Categoria;

public class CategoriaMapper {

    public static Categoria toEntity(CategoriaRequest dto) {
        return Categoria.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .activo(true)
                .build();
    }

    public static void updateEntity(Categoria categoria, CategoriaRequest dto) {
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
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
