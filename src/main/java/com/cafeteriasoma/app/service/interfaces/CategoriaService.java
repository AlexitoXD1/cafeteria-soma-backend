package com.cafeteriasoma.app.service.interfaces;

import java.util.List;

import com.cafeteriasoma.app.dto.categoria.CategoriaRequest;
import com.cafeteriasoma.app.dto.categoria.CategoriaResponse;

public interface CategoriaService {

    CategoriaResponse crearCategoria(CategoriaRequest request);

    List<CategoriaResponse> listarCategorias(boolean soloActivas);

    // Admin: obtiene por ID (aunque esté inactiva)
    CategoriaResponse obtenerPorId(Long idCategoria);

    // Público: solo categorías activas
    CategoriaResponse obtenerActivaPorId(Long idCategoria);

    CategoriaResponse actualizarCategoria(Long idCategoria, CategoriaRequest request);

    void cambiarEstado(Long idCategoria, Boolean activo);
}
