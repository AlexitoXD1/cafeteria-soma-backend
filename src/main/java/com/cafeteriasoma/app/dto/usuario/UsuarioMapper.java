package com.cafeteriasoma.app.dto.usuario;

import com.cafeteriasoma.app.entity.Usuario;

public class UsuarioMapper {

    public static UsuarioResponse toResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .telefono(usuario.getTelefono())
                .idRol(usuario.getRol().getIdRol())
                .rol(usuario.getRol().getNombre())
                .activo(usuario.getActivo())
                .fechaCreacion(usuario.getFechaCreacion())
                .build();
    }
}

