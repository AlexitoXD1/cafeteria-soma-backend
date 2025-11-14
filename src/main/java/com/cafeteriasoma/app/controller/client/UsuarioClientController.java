package com.cafeteriasoma.app.controller.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cafeteriasoma.app.dto.usuario.UsuarioResponse;
import com.cafeteriasoma.app.dto.usuario.UsuarioUpdateRequest;
import com.cafeteriasoma.app.service.interfaces.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/client/usuario")
@RequiredArgsConstructor
public class UsuarioClientController {

    private final UsuarioService usuarioService;

    @GetMapping("/perfil")
    public ResponseEntity<UsuarioResponse> obtenerPerfil(@RequestParam String correo) {
        return ResponseEntity.ok(usuarioService.obtenerPerfil(correo));
    }

    @PutMapping("/perfil")
    public ResponseEntity<UsuarioResponse> actualizarPerfil(@RequestParam String correo,
                                                            @RequestBody UsuarioUpdateRequest request) {
        return ResponseEntity.ok(usuarioService.actualizarPerfil(correo, request));
    }
}
