package com.cafeteriasoma.app.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cafeteriasoma.app.dto.usuario.UsuarioMapper;
import com.cafeteriasoma.app.dto.usuario.UsuarioRequest;
import com.cafeteriasoma.app.dto.usuario.UsuarioResponse;
import com.cafeteriasoma.app.dto.usuario.UsuarioUpdateRequest;
import com.cafeteriasoma.app.entity.Rol;
import com.cafeteriasoma.app.entity.Usuario;
import com.cafeteriasoma.app.exception.ConflictException;
import com.cafeteriasoma.app.exception.ResourceNotFoundException;
import com.cafeteriasoma.app.exception.UnauthorizedException;
import com.cafeteriasoma.app.repository.RolRepository;
import com.cafeteriasoma.app.repository.UsuarioRepository;
import com.cafeteriasoma.app.service.interfaces.UsuarioService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    // ====================== ADMIN ======================
    @Override
    public UsuarioResponse crearUsuario(UsuarioRequest request) {
        if (usuarioRepository.findByCorreo(request.getCorreo()).isPresent()) {
            throw new ConflictException("Ya existe un usuario con ese correo");
        }

        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .correo(request.getCorreo())
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
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return UsuarioMapper.toResponse(usuario);
    }

    @Override
    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioMapper::toResponse)
                .toList();
    }

    @Override
    public UsuarioResponse actualizarUsuario(Long idUsuario, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));

        usuario.setNombre(request.getNombre());
        usuario.setTelefono(request.getTelefono());
        usuario.setRol(rol);

        return UsuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    public void cambiarEstado(Long idUsuario, Boolean activo) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        usuario.setActivo(activo);
        usuarioRepository.save(usuario);
    }

    // ====================== CLIENTE ======================
    @Override
    public UsuarioResponse obtenerPerfil(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return UsuarioMapper.toResponse(usuario);
    }

    @Override
    public UsuarioResponse actualizarPerfil(String correo, UsuarioUpdateRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        usuario.setNombre(request.getNombre());
        usuario.setTelefono(request.getTelefono());

        return UsuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    public void cambiarContrasena(String correo, String actual, String nueva) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(actual, usuario.getContrasena())) {
            throw new UnauthorizedException("La contraseña actual es incorrecta");
        }
    
        usuario.setContrasena(passwordEncoder.encode(nueva));
        usuarioRepository.save(usuario);
    }
}

