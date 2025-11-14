package com.cafeteriasoma.app.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cafeteriasoma.app.dto.usuario.UsuarioMapper;
import com.cafeteriasoma.app.dto.usuario.UsuarioRequest;
import com.cafeteriasoma.app.dto.usuario.UsuarioResponse;
import com.cafeteriasoma.app.dto.usuario.UsuarioUpdateRequest;
import com.cafeteriasoma.app.entity.Rol;
import com.cafeteriasoma.app.entity.Usuario;
import com.cafeteriasoma.app.repository.RolRepository;
import com.cafeteriasoma.app.repository.UsuarioRepository;
import com.cafeteriasoma.app.service.interfaces.UsuarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    // ADMIN: crear nuevo usuario
    @Override
    public UsuarioResponse crearUsuario(UsuarioRequest request) {
        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        if (usuarioRepository.findByCorreo(request.getCorreo()).isPresent()) {
            throw new RuntimeException("Ya existe un usuario con ese correo");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .correo(request.getCorreo().toLowerCase())
                .telefono(request.getTelefono())
                .contrasena(passwordEncoder.encode(request.getContrasena()))
                .rol(rol)
                .activo(true)
                .build();

        return UsuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    public UsuarioResponse obtenerPorId(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return UsuarioMapper.toResponse(usuario);
    }

    @Override
    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioResponse actualizarUsuario(Long idUsuario, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        usuario.setNombre(request.getNombre());
        usuario.setTelefono(request.getTelefono());
        usuario.setRol(rol);

        if (request.getContrasena() != null && !request.getContrasena().isBlank()) {
            usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        }

        return UsuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    public void cambiarEstado(Long idUsuario, Boolean activo) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setActivo(activo);
        usuarioRepository.save(usuario);
    }

    // CLIENTE: obtener y actualizar perfil
    @Override
    public UsuarioResponse obtenerPerfil(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return UsuarioMapper.toResponse(usuario);
    }

    @Override
    public UsuarioResponse actualizarPerfil(String correo, UsuarioUpdateRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre(request.getNombre());
        usuario.setTelefono(request.getTelefono());

        if (request.getContrasenaNueva() != null && !request.getContrasenaNueva().isBlank()) {
            usuario.setContrasena(passwordEncoder.encode(request.getContrasenaNueva()));
        }

        return UsuarioMapper.toResponse(usuarioRepository.save(usuario));
    }
}
