package com.cafeteriasoma.app.controller.client;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cafeteriasoma.app.dto.producto.ProductoResponse;
import com.cafeteriasoma.app.service.interfaces.ProductoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/client/productos")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('CLIENTE')")
public class ProductoClientController {

    private final ProductoService productoService;

    // Listar todos los productos activos
    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listarActivos() {
        return ResponseEntity.ok(productoService.listarProductosActivos());
    }

    // Listar productos por categoría
    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<ProductoResponse>> listarPorCategoria(@PathVariable Long idCategoria) {
        return ResponseEntity.ok(productoService.listarPorCategoria(idCategoria));
    }

    // Obtener detalle de producto
    @GetMapping("/{idProducto}")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long idProducto) {
        return productoService.obtenerPorId(idProducto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
