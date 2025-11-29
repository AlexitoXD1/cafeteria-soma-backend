package com.cafeteriasoma.app.controller.publico;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cafeteriasoma.app.dto.producto.ProductoResponse;
import com.cafeteriasoma.app.service.interfaces.ProductoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public/productos")
@RequiredArgsConstructor
public class ProductoPublicController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listarActivos() {
        return ResponseEntity.ok(productoService.listarProductosActivos());
    }

    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<ProductoResponse>> listarPorCategoria(@PathVariable Long idCategoria) {
        return ResponseEntity.ok(productoService.listarPorCategoria(idCategoria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }
}