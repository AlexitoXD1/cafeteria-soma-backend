package com.cafeteriasoma.app.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cafeteriasoma.app.dto.categoria.mapper.CategoriaMapper;
import com.cafeteriasoma.app.dto.categoria.request.CategoriaRequest;
import com.cafeteriasoma.app.dto.categoria.response.CategoriaResponse;
import com.cafeteriasoma.app.entity.Categoria;
import com.cafeteriasoma.app.exception.ResourceConflictException;
import com.cafeteriasoma.app.exception.ResourceNotFoundException;
import com.cafeteriasoma.app.repository.CategoriaRepository;
import com.cafeteriasoma.app.service.interfaces.CategoriaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    @Transactional
    public CategoriaResponse crearCategoria(CategoriaRequest request) {
        String nombre = normalizarNombre(request.getNombre());
        String descripcion = normalizarDescripcion(request.getDescripcion());

        validarNombreDisponible(nombre, null);

        Categoria guardada = categoriaRepository.save(CategoriaMapper.toEntity(nombre, descripcion));
        return CategoriaMapper.toResponse(guardada);
    }

    @Override
    public List<CategoriaResponse> listarCategorias(boolean soloActivas) {
        List<Categoria> categorias = soloActivas
                ? categoriaRepository.findAllByActivoTrueOrderByNombreAsc()
                : categoriaRepository.findAllByOrderByNombreAsc();

        return categorias.stream()
                .map(CategoriaMapper::toResponse)
                .toList();
    }

    @Override
    public CategoriaResponse obtenerPorId(Long idCategoria) {
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new ResourceNotFoundException("La categoría solicitada no existe."));
        return CategoriaMapper.toResponse(categoria);
    }

    @Override
    public CategoriaResponse obtenerActivaPorId(Long idCategoria) {
        Categoria categoria = categoriaRepository.findByIdAndActivoTrue(idCategoria)
                .orElseThrow(() -> new ResourceNotFoundException("La categoría solicitada no existe o está inactiva."));
        return CategoriaMapper.toResponse(categoria);
    }

    @Override
    @Transactional
    public CategoriaResponse actualizarCategoria(Long idCategoria, CategoriaRequest request) {
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new ResourceNotFoundException("La categoría solicitada no existe."));

        String nombre = normalizarNombre(request.getNombre());
        String descripcion = normalizarDescripcion(request.getDescripcion());

        validarNombreDisponible(nombre, idCategoria);

        CategoriaMapper.updateEntity(categoria, nombre, descripcion);
        return CategoriaMapper.toResponse(categoria);
    }

    @Override
    @Transactional
    public void cambiarEstado(Long idCategoria, boolean activo) {
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new ResourceNotFoundException("La categoría solicitada no existe."));

        if (categoria.getActivo() != activo) {
            categoria.setActivo(activo);
        }
    }

    private void validarNombreDisponible(String nombreNormalizado, Long idActual) {
        boolean nombreUsado = (idActual == null)
                ? categoriaRepository.existsByNombreIgnoreCase(nombreNormalizado)
                : categoriaRepository.existsByNombreIgnoreCaseAndIdCategoriaNot(nombreNormalizado, idActual);

        if (nombreUsado) {
            throw new ResourceConflictException("Ya existe una categoría registrada con el nombre proporcionado.");
        }
    }

    private String normalizarNombre(String nombre) {
        return StringUtils.trimWhitespace(nombre);
    }

    private String normalizarDescripcion(String descripcion) {
        String valor = StringUtils.trimWhitespace(descripcion);
        return StringUtils.hasText(valor) ? valor : null;
    }
}
