package com.cafeteriasoma.app.controller.client;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cafeteriasoma.app.dto.venta.VentaRequest;
import com.cafeteriasoma.app.dto.venta.VentaResponse;
import com.cafeteriasoma.app.service.interfaces.VentaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/client/ventas")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('CLIENTE')")
public class VentaClientController {

    private final VentaService ventaService;

    private String obtenerCorreoJwt() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }

    // Crear venta a partir del carrito
    @PostMapping
    public ResponseEntity<VentaResponse> crear(@Valid @RequestBody VentaRequest request) {
        VentaResponse response = ventaService.crearVenta(obtenerCorreoJwt(), request);
        return ResponseEntity.status(201).body(response);
    }

    // Listar mis ventas
    @GetMapping
    public ResponseEntity<List<VentaResponse>> listarMisVentas() {
        return ResponseEntity.ok(ventaService.listarMisVentas(obtenerCorreoJwt()));
    }
}
