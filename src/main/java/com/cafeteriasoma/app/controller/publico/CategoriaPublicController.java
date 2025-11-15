package com.cafeteriasoma.app.controller.publico;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cafeteriasoma.app.dto.categoria.CategoriaResponse;
import com.cafeteriasoma.app.service.interfaces.CategoriaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public/categorias")
@RequiredArgsConstructor
public class CategoriaPublicController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarActivas() {
        return ResponseEntity.ok(categoriaService.listarCategorias(true));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.obtenerActivaPorId(id));
    }
}
