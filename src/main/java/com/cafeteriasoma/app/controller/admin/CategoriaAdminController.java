package com.cafeteriasoma.app.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cafeteriasoma.app.dto.categoria.request.CategoriaRequest;
import com.cafeteriasoma.app.dto.categoria.response.CategoriaResponse;
import com.cafeteriasoma.app.service.interfaces.CategoriaService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/categorias")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAuthority('ADMIN')")
public class CategoriaAdminController {

    private final CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<CategoriaResponse> crearCategoria(@Valid @RequestBody CategoriaRequest request) {
        CategoriaResponse response = categoriaService.crearCategoria(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.listarCategorias(false));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> obtenerCategoria(@PathVariable("id") @Positive Long idCategoria) {
        return ResponseEntity.ok(categoriaService.obtenerPorId(idCategoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> actualizarCategoria(
            @PathVariable("id") @Positive Long idCategoria,
            @Valid @RequestBody CategoriaRequest request
    ) {
        return ResponseEntity.ok(categoriaService.actualizarCategoria(idCategoria, request));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable("id") @Positive Long idCategoria,
            @RequestParam("activo") boolean activo
    ) {
        categoriaService.cambiarEstado(idCategoria, activo);
        return ResponseEntity.noContent().build();
    }
}
