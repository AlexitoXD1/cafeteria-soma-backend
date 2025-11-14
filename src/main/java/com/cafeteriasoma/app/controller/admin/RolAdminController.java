package com.cafeteriasoma.app.controller.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cafeteriasoma.app.dto.rol.RolRequest;
import com.cafeteriasoma.app.dto.rol.RolResponse;
import com.cafeteriasoma.app.service.interfaces.RolService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/roles")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class RolAdminController {

    private final RolService rolService;

    @PostMapping
    public ResponseEntity<RolResponse> crear(@RequestBody RolRequest request) {
        return ResponseEntity.ok(rolService.crearRol(request));
    }

    @GetMapping
    public ResponseEntity<List<RolResponse>> listar() {
        return ResponseEntity.ok(rolService.listarRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(rolService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolResponse> actualizar(@PathVariable Long id, @RequestBody RolRequest request) {
        return ResponseEntity.ok(rolService.actualizarRol(id, request));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestParam Boolean activo) {
        rolService.cambiarEstado(id, activo);
        return ResponseEntity.noContent().build();
    }
}
