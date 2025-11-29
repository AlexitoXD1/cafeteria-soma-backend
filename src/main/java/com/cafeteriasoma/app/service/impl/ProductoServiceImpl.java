package com.cafeteriasoma.app.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cafeteriasoma.app.dto.producto.ProductoMapper;
import com.cafeteriasoma.app.dto.producto.ProductoRequest;
import com.cafeteriasoma.app.dto.producto.ProductoResponse;
import com.cafeteriasoma.app.entity.Categoria;
import com.cafeteriasoma.app.entity.Producto;
import com.cafeteriasoma.app.exception.BadRequestException;
import com.cafeteriasoma.app.exception.ConflictException;
import com.cafeteriasoma.app.exception.ResourceNotFoundException;
import com.cafeteriasoma.app.repository.CategoriaRepository;
import com.cafeteriasoma.app.repository.ProductoRepository;
import com.cafeteriasoma.app.service.interfaces.ProductoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    @Override
    public ProductoResponse crearProducto(ProductoRequest request) {

        if (productoRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new ConflictException("Ya existe un producto con el nombre: " + request.getNombre());
        }

        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() ->
                        new ResourceNotFoundException("La categoría especificada no existe.")
                );

        if (!categoria.getActivo()) {
            throw new BadRequestException("No se puede crear un producto en una categoría inactiva.");
        }

        Producto producto = ProductoMapper.toEntity(request, categoria);

        return ProductoMapper.toResponse(productoRepository.save(producto));
    }

    @Override
    public ProductoResponse actualizarProducto(Long idProducto, ProductoRequest request) {

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado con id: " + idProducto)
                );

        if (productoRepository.existsByNombreIgnoreCaseAndIdProductoNot(request.getNombre(), idProducto)) {
            throw new ConflictException("Ya existe otro producto con el nombre: " + request.getNombre());
        }

        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() ->
                        new ResourceNotFoundException("La categoría especificada no existe.")
                );

        if (!categoria.getActivo()) {
            throw new BadRequestException("La categoría seleccionada está inactiva.");
        }

        ProductoMapper.updateEntity(producto, request, categoria);

        return ProductoMapper.toResponse(productoRepository.save(producto));
    }

    @Override
    public void cambiarEstadoProducto(Long idProducto, boolean activo) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado con id: " + idProducto)
                );

        producto.setActivo(activo);
        productoRepository.save(producto);
    }

    // PUBLICO / CLIENTE

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listarProductosActivos() {
        return productoRepository.findByActivoTrue()
                .stream()
                .map(ProductoMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listarPorCategoria(Long idCategoria) {

        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoría no encontrada.")
                );

        if (!categoria.getActivo()) {
            throw new BadRequestException("Esta categoría está inactiva.");
        }

        return productoRepository.findByCategoria_IdCategoriaAndActivoTrue(idCategoria)
                .stream()
                .map(ProductoMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long idProducto) {
        return productoRepository.findByIdAndActivoTrue(idProducto)
                .map(ProductoMapper::toResponse)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Producto no encontrado o inactivo.")
                );
    }
}
