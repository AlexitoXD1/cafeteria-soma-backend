package com.cafeteriasoma.app.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cafeteriasoma.app.dto.producto.ProductoRequest;
import com.cafeteriasoma.app.dto.producto.ProductoResponse;
import com.cafeteriasoma.app.service.interfaces.ProductoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/productos")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class ProductoAdminController {

    private final ProductoService productoService;

    // Crear producto
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(productoService.crearProducto(request));
    }

    // Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequest request
    ) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, request));
    }

    // Cambiar estado (activar/desactivar)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable Long id,
            @RequestParam boolean activo
    ) {
        productoService.cambiarEstadoProducto(id, activo);
        return ResponseEntity.noContent().build();
    }
}
