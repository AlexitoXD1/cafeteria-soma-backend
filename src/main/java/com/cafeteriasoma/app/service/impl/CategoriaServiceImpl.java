package com.cafeteriasoma.app.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cafeteriasoma.app.dto.categoria.CategoriaMapper;
import com.cafeteriasoma.app.dto.categoria.CategoriaRequest;
import com.cafeteriasoma.app.dto.categoria.CategoriaResponse;
import com.cafeteriasoma.app.entity.Categoria;
import com.cafeteriasoma.app.exception.BadRequestException;
import com.cafeteriasoma.app.exception.ConflictException;
import com.cafeteriasoma.app.exception.ResourceNotFoundException;
import com.cafeteriasoma.app.repository.CategoriaRepository;
import com.cafeteriasoma.app.service.interfaces.CategoriaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    public CategoriaResponse crearCategoria(CategoriaRequest request) {
        if (categoriaRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new ConflictException("Ya existe una categoría con el nombre: " + request.getNombre());
        }

        Categoria categoria = CategoriaMapper.toEntity(request);
        Categoria guardada = categoriaRepository.save(categoria);
        return CategoriaMapper.toResponse(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponse> listarCategorias(boolean soloActivas) {
        List<Categoria> categorias = soloActivas
                ? categoriaRepository.findByActivoTrue()
                : categoriaRepository.findAll();

        return categorias.stream()
                .map(CategoriaMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponse obtenerPorId(Long idCategoria) {
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoría no encontrada con id: " + idCategoria)
                );
        return CategoriaMapper.toResponse(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponse obtenerActivaPorId(Long idCategoria) {
        Categoria categoria = categoriaRepository.findByIdAndActivoTrue(idCategoria)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoría no encontrada o inactiva con id: " + idCategoria)
                );
        return CategoriaMapper.toResponse(categoria);
    }

    @Override
    public CategoriaResponse actualizarCategoria(Long idCategoria, CategoriaRequest request) {
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoría no encontrada con id: " + idCategoria)
                );

        if (categoriaRepository.existsByNombreIgnoreCaseAndIdCategoriaNot(request.getNombre(), idCategoria)) {
            throw new ConflictException("Ya existe una categoría con el nombre: " + request.getNombre());
        }

        CategoriaMapper.updateEntity(categoria, request);
        Categoria actualizada = categoriaRepository.save(categoria);
        return CategoriaMapper.toResponse(actualizada);
    }

    @Override
    public void cambiarEstado(Long idCategoria, Boolean activo) {
        if (activo == null) {
            throw new BadRequestException("El parámetro 'activo' es obligatorio");
        }

        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoría no encontrada con id: " + idCategoria)
                );

        categoria.setActivo(activo);
        categoriaRepository.save(categoria);
    }
}
