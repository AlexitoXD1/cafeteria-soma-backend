package com.cafeteriasoma.app.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
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

import com.cafeteriasoma.app.dto.producto.ProductoRequest;
import com.cafeteriasoma.app.dto.producto.ProductoResponse;
import com.cafeteriasoma.app.service.interfaces.ProductoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador administrativo para la gestión de productos.
 * Requiere autoridad ADMIN para acceder a todos los endpoints.
 */
@RestController
@RequestMapping("/api/admin/productos")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class ProductoAdminController {

    private final ProductoService productoService;

    /**
     * Listar productos (activos, inactivos o todos según parámetro).
     *
     * @param activo - parámetro opcional:
     *               - true: solo productos activos
     *               - false: solo productos inactivos
     *               - null/omitido: todos los productos
     * @return lista de ProductoResponse
     */
    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listarProductos(
            @RequestParam(required = false) Boolean activo
    ) {
        List<ProductoResponse> productos;

        if (activo == null) {
            // Si no se especifica, devolver todos
            productos = productoService.listarTodosProductos();
        } else if (activo) {
            // Solo activos
            productos = productoService.listarProductosActivos();
        } else {
            // Solo inactivos (necesitarás agregar este método al servicio)
            productos = productoService.listarProductosInactivos();
        }

        return ResponseEntity.ok(productos);
    }

    /**
     * Obtener un producto activo por su ID.
     *
     * @param id ID del producto
     * @return ProductoResponse del producto solicitado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    /**
     * Crear un nuevo producto (activo por defecto).
     *
     * @param request datos del producto a crear
     * @return ProductoResponse del producto creado
     */
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        ProductoResponse response = productoService.crearProducto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Actualizar los detalles de un producto existente.
     *
     * @param id      ID del producto a actualizar
     * @param request nuevos datos del producto
     * @return ProductoResponse del producto actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequest request
    ) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, request));
    }

    /**
     * Cambiar el estado (activo/inactivo) de un producto.
     *
     * @param id     ID del producto
     * @param activo nuevo estado (true = activo, false = inactivo)
     * @return 204 No Content
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable Long id,
            @RequestParam boolean activo
    ) {
        productoService.cambiarEstadoProducto(id, activo);
        return ResponseEntity.noContent().build();
    }
}
