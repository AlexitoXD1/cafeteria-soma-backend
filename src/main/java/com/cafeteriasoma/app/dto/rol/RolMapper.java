package com.cafeteriasoma.app.dto.rol;

import com.cafeteriasoma.app.entity.Rol;

public class RolMapper {

    public static Rol toEntity(RolRequest dto) {
        return Rol.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .activo(dto.getActivo() != null ? dto.getActivo() : true)
                .build();
    }

    public static RolResponse toResponse(Rol rol) {
        return RolResponse.builder()
                .idRol(rol.getIdRol())
                .nombre(rol.getNombre())
                .descripcion(rol.getDescripcion())
                .activo(rol.getActivo())
                .fechaCreacion(rol.getFechaCreacion())
                .fechaActualizacion(rol.getFechaActualizacion())
                .build();
    }
}
