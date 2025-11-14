package com.cafeteriasoma.app.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cafeteriasoma.app.dto.producto.ProductoMapper;
import com.cafeteriasoma.app.dto.producto.ProductoRequest;
import com.cafeteriasoma.app.dto.producto.ProductoResponse;
import com.cafeteriasoma.app.entity.Categoria;
import com.cafeteriasoma.app.entity.Producto;
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

    // MÉTODOS ADMIN

    @Override
    public ProductoResponse crearProducto(ProductoRequest request) {
        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new IllegalArgumentException("La categoría especificada no existe."));

        if (!categoria.getActivo()) {
            throw new RuntimeException("No se puede asignar un producto a una categoría inactiva");
        }

        Producto producto = Producto.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .precio(request.getPrecio())
                .stock(request.getStock())
                .imagenUrl(request.getImagenUrl())
                .categoria(categoria)
                .activo(true)
                .build();

        return ProductoMapper.toResponse(productoRepository.save(producto));
    }

    @Override
    public ProductoResponse actualizarProducto(Long idProducto, ProductoRequest request) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("El producto no existe."));

        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new IllegalArgumentException("La categoría especificada no existe."));

        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setImagenUrl(request.getImagenUrl());
        producto.setCategoria(categoria);

        Producto actualizado = productoRepository.save(producto);
        return ProductoMapper.toResponse(actualizado);
    }

    @Override
    public void cambiarEstadoProducto(Long idProducto, boolean activo) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("El producto no existe."));

        producto.setActivo(activo);
        productoRepository.save(producto);
    }

    // MÉTODOS CLIENTE / PÚBLICO

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listarProductosActivos() {
        return productoRepository.findAll()
                .stream()
                .filter(Producto::getActivo)
                .map(ProductoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listarPorCategoria(Long idCategoria) {
        return productoRepository.findAll()
                .stream()
                .filter(p -> p.getActivo() && p.getCategoria().getIdCategoria().equals(idCategoria))
                .map(ProductoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoResponse> obtenerPorId(Long idProducto) {
        return productoRepository.findById(idProducto)
                .filter(Producto::getActivo)
                .map(ProductoMapper::toResponse);
    }
}
