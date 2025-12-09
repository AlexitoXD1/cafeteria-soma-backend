package com.cafeteriasoma.app.controller.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cafeteriasoma.app.dto.venta.VentaResponse;
import com.cafeteriasoma.app.service.interfaces.VentaService;

import lombok.RequiredArgsConstructor;

// Admin controller for managing sales
@RestController
@RequestMapping("/api/admin/ventas")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class VentaAdminController {

    private final VentaService ventaService;

    @GetMapping
    public ResponseEntity<List<VentaResponse>> listarTodas() {
        return ResponseEntity.ok(ventaService.listarTodas());
    }
}
