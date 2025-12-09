package com.cafeteriasoma.app.controller.admin;

import com.cafeteriasoma.app.dto.promocion.PromocionRequest;
import com.cafeteriasoma.app.dto.promocion.PromocionResponse;
import com.cafeteriasoma.app.service.interfaces.PromocionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Admin controller for managing promotions
@RestController
@RequestMapping("/api/admin/promociones")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class PromocionAdminController {

    private final PromocionService promocionService;

    /**
     * List all registered promotions.
     * GET /api/admin/promociones
     */
    @GetMapping
    public ResponseEntity<List<PromocionResponse>> listarTodas() {
        return ResponseEntity.ok(promocionService.listarTodas());
    }

    /**
     * Obtener una promoción específica por su ID.
     * GET /api/admin/promociones/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<PromocionResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(promocionService.obtenerPorId(id));
    }

    /**
     * Crear una nueva promoción.
     * POST /api/admin/promociones
     * Requiere Body JSON (PromocionRequest)
     */
    @PostMapping
    public ResponseEntity<PromocionResponse> crear(@Valid @RequestBody PromocionRequest request) {
        return ResponseEntity.status(201).body(promocionService.crearPromocion(request));
    }

    /**
     * Actualizar una promoción existente.
     * PUT /api/admin/promociones/{id}
     * Requiere Body JSON (PromocionRequest)
     */
    @PutMapping("/{id}")
    public ResponseEntity<PromocionResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PromocionRequest request
    ) {
        return ResponseEntity.ok(promocionService.actualizarPromocion(id, request));
    }

    /**
     * Activar o desactivar una promoción rápidamente.
     * PATCH /api/admin/promociones/{id}/estado?activo=true
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable Long id,
            @RequestParam boolean activo
    ) {
        promocionService.cambiarEstado(id, activo);
        return ResponseEntity.noContent().build();
    }

    /**
     * Eliminar una promoción (Borrado físico).
     * DELETE /api/admin/promociones/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        promocionService.eliminarPromocion(id);
        return ResponseEntity.noContent().build();
    }
}