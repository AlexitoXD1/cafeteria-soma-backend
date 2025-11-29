package com.cafeteriasoma.app.service.interfaces;

import java.util.List;

import com.cafeteriasoma.app.dto.rol.RolRequest;
import com.cafeteriasoma.app.dto.rol.RolResponse;

public interface RolService {
    RolResponse crearRol(RolRequest request);
    List<RolResponse> listarRoles();
    RolResponse obtenerPorId(Long idRol);
    RolResponse actualizarRol(Long idRol, RolRequest request);
    void cambiarEstado(Long idRol, Boolean activo);
}
