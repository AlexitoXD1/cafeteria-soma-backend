package com.cafeteriasoma.app.service.interfaces;

import java.util.List;

import com.cafeteriasoma.app.dto.categoria.request.CategoriaRequest;
import com.cafeteriasoma.app.dto.categoria.response.CategoriaResponse;

public interface CategoriaService {
    CategoriaResponse crearCategoria(CategoriaRequest request);
    List<CategoriaResponse> listarCategorias(boolean soloActivas);
    CategoriaResponse obtenerPorId(Long idCategoria);
    CategoriaResponse obtenerActivaPorId(Long idCategoria);
    CategoriaResponse actualizarCategoria(Long idCategoria, CategoriaRequest request);
    void cambiarEstado(Long idCategoria, boolean activo);
}
