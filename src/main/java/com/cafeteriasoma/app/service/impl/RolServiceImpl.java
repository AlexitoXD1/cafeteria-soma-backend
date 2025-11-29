package com.cafeteriasoma.app.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cafeteriasoma.app.dto.rol.RolMapper;
import com.cafeteriasoma.app.dto.rol.RolRequest;
import com.cafeteriasoma.app.dto.rol.RolResponse;
import com.cafeteriasoma.app.entity.Rol;
import com.cafeteriasoma.app.repository.RolRepository;
import com.cafeteriasoma.app.service.interfaces.RolService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    @Override
    public RolResponse crearRol(RolRequest request) {
        Rol rol = RolMapper.toEntity(request);
        Rol guardado = rolRepository.save(rol);
        return RolMapper.toResponse(guardado);
    }

    @Override
    public List<RolResponse> listarRoles() {
        return rolRepository.findAll()
                .stream()
                .map(RolMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RolResponse obtenerPorId(Long idRol) {
        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        return RolMapper.toResponse(rol);
    }

    @Override
    public RolResponse actualizarRol(Long idRol, RolRequest request) {
        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        rol.setNombre(request.getNombre());
        rol.setDescripcion(request.getDescripcion());
        rol.setActivo(request.getActivo());
        Rol actualizado = rolRepository.save(rol);
        return RolMapper.toResponse(actualizado);
    }

    @Override
    public void cambiarEstado(Long idRol, Boolean activo) {
        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        rol.setActivo(activo);
        rolRepository.save(rol);
    }
}
