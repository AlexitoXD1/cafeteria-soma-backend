package com.cafeteriasoma.app.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.cafeteriasoma.app.dto.producto.ProductoRequest;
import com.cafeteriasoma.app.dto.producto.ProductoResponse;

public interface ProductoService {

    // ADMIN
    ProductoResponse crearProducto(ProductoRequest request);
    ProductoResponse actualizarProducto(Long idProducto, ProductoRequest request);
    void cambiarEstadoProducto(Long idProducto, boolean activo);

    // CLIENTE / PÚBLICO
    List<ProductoResponse> listarProductosActivos();
    List<ProductoResponse> listarPorCategoria(Long idCategoria);
    Optional<ProductoResponse> obtenerPorId(Long idProducto);
}
