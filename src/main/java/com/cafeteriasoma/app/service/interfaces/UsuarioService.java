package com.cafeteriasoma.app.service.interfaces;

import java.util.List;

import com.cafeteriasoma.app.dto.usuario.UsuarioRequest;
import com.cafeteriasoma.app.dto.usuario.UsuarioResponse;
import com.cafeteriasoma.app.dto.usuario.UsuarioUpdateRequest;

public interface UsuarioService {

    // ADMIN
    UsuarioResponse crearUsuario(UsuarioRequest request);
    UsuarioResponse obtenerPorId(Long idUsuario);
    List<UsuarioResponse> listarUsuarios();
    UsuarioResponse actualizarUsuario(Long idUsuario, UsuarioRequest request);
    void cambiarEstado(Long idUsuario, Boolean activo);

    // CLIENTE (desde JWT)
    UsuarioResponse obtenerPerfil(String correo);
    UsuarioResponse actualizarPerfil(String correo, UsuarioUpdateRequest request);
    void cambiarContrasena(String correo, String contrasenaActual, String contrasenaNueva);
}

