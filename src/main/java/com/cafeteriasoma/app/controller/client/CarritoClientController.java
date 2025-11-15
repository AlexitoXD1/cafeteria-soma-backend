package com.cafeteriasoma.app.controller.client;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cafeteriasoma.app.dto.carrito.CarritoItemRequest;
import com.cafeteriasoma.app.dto.carrito.CarritoItemResponse;
import com.cafeteriasoma.app.service.interfaces.CarritoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/client/carrito")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('CLIENTE')")
public class CarritoClientController {

    private final CarritoService carritoService;

    private String obtenerCorreoJwt() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    // Listar carrito del usuario
    @GetMapping
    public ResponseEntity<List<CarritoItemResponse>> obtenerCarrito() {
        return ResponseEntity.ok(carritoService.obtenerCarrito(obtenerCorreoJwt()));
    }

    // Agregar producto al carrito (o incrementar cantidad)
    @PostMapping
    public ResponseEntity<CarritoItemResponse> agregar(@Valid @RequestBody CarritoItemRequest request) {
        return ResponseEntity.ok(carritoService.agregarAlCarrito(obtenerCorreoJwt(), request));
    }

    // Actualizar cantidad de un producto en el carrito
    @PutMapping
    public ResponseEntity<CarritoItemResponse> actualizar(@Valid @RequestBody CarritoItemRequest request) {
        return ResponseEntity.ok(carritoService.actualizarCantidad(obtenerCorreoJwt(), request));
    }

    // Eliminar un producto del carrito
    @DeleteMapping("/{idProducto}")
    public ResponseEntity<Void> eliminar(@PathVariable Long idProducto) {
        carritoService.eliminarItem(obtenerCorreoJwt(), idProducto);
        return ResponseEntity.noContent().build();
    }

    // Vaciar todo el carrito
    @DeleteMapping
    public ResponseEntity<Void> vaciar() {
        carritoService.vaciarCarrito(obtenerCorreoJwt());
        return ResponseEntity.noContent().build();
    }
}
