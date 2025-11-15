package com.cafeteriasoma.app.controller.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cafeteriasoma.app.dto.usuario.UsuarioResponse;
import com.cafeteriasoma.app.dto.usuario.UsuarioUpdateRequest;
import com.cafeteriasoma.app.service.interfaces.UsuarioService;
import com.cafeteriasoma.app.dto.usuario.UsuarioChangePasswordRequest;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/client/usuario")
@RequiredArgsConstructor
public class UsuarioClientController {

    private final UsuarioService usuarioService;

    private String obtenerCorreoJwt() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName(); // correo del usuario autenticado
    }

    @GetMapping("/perfil")
    public ResponseEntity<UsuarioResponse> obtenerPerfil() {
        return ResponseEntity.ok(usuarioService.obtenerPerfil(obtenerCorreoJwt()));
    }

    @PutMapping("/perfil")
    public ResponseEntity<UsuarioResponse> actualizarPerfil(
            @RequestBody UsuarioUpdateRequest request) {
        return ResponseEntity.ok(usuarioService.actualizarPerfil(obtenerCorreoJwt(), request));
    }

    @PutMapping("/perfil/contrasena")
    public ResponseEntity<Void> cambiarContrasena(
            @RequestBody UsuarioChangePasswordRequest request) {

        usuarioService.cambiarContrasena(
                obtenerCorreoJwt(),
                request.getContrasenaActual(),
                request.getContrasenaNueva()
        );
        return ResponseEntity.noContent().build();
    }
}

