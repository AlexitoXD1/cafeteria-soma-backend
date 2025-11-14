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

import com.cafeteriasoma.app.dto.categoria.CategoriaRequest;
import com.cafeteriasoma.app.dto.categoria.CategoriaResponse;
import com.cafeteriasoma.app.service.interfaces.CategoriaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/categorias")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class CategoriaAdminController {

    private final CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<CategoriaResponse> crear(@RequestBody CategoriaRequest request) {
        return ResponseEntity.ok(categoriaService.crearCategoria(request));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listar() {
        return ResponseEntity.ok(categoriaService.listarCategorias(false));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> actualizar(@PathVariable Long id, @RequestBody CategoriaRequest request) {
        return ResponseEntity.ok(categoriaService.actualizarCategoria(id, request));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestParam Boolean activo) {
        categoriaService.cambiarEstado(id, activo);
        return ResponseEntity.noContent().build();
    }
}
