package com.cafeteriasoma.app.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cafeteriasoma.app.dto.categoria.CategoriaMapper;
import com.cafeteriasoma.app.dto.categoria.CategoriaRequest;
import com.cafeteriasoma.app.dto.categoria.CategoriaResponse;
import com.cafeteriasoma.app.entity.Categoria;
import com.cafeteriasoma.app.repository.CategoriaRepository;
import com.cafeteriasoma.app.service.interfaces.CategoriaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    public CategoriaResponse crearCategoria(CategoriaRequest request) {
        Categoria categoria = CategoriaMapper.toEntity(request);
        Categoria guardada = categoriaRepository.save(categoria);
        return CategoriaMapper.toResponse(guardada);
    }

    @Override
    public List<CategoriaResponse> listarCategorias(boolean soloActivas) {
        List<Categoria> categorias = soloActivas
                ? categoriaRepository.findAll().stream()
                    .filter(Categoria::getActivo)
                    .collect(Collectors.toList())
                : categoriaRepository.findAll();

        return categorias.stream()
                .map(CategoriaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoriaResponse obtenerPorId(Long idCategoria) {
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return CategoriaMapper.toResponse(categoria);
    }

    @Override
    public CategoriaResponse actualizarCategoria(Long idCategoria, CategoriaRequest request) {
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());

        return CategoriaMapper.toResponse(categoriaRepository.save(categoria));
    }

    @Override
    public void cambiarEstado(Long idCategoria, Boolean activo) {
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        categoria.setActivo(activo);
        categoriaRepository.save(categoria);
    }
}
