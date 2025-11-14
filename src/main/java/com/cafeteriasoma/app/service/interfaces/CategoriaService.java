package com.cafeteriasoma.app.service.interfaces;

import java.util.List;

import com.cafeteriasoma.app.dto.categoria.CategoriaRequest;
import com.cafeteriasoma.app.dto.categoria.CategoriaResponse;

public interface CategoriaService {
    CategoriaResponse crearCategoria(CategoriaRequest request);
    List<CategoriaResponse> listarCategorias(boolean soloActivas);
    CategoriaResponse obtenerPorId(Long idCategoria);
    CategoriaResponse actualizarCategoria(Long idCategoria, CategoriaRequest request);
    void cambiarEstado(Long idCategoria, Boolean activo);
}
