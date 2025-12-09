package com.cafeteriasoma.app.service.interfaces;

import java.util.List;

import com.cafeteriasoma.app.dto.producto.ProductoRequest;
import com.cafeteriasoma.app.dto.producto.ProductoResponse;

public interface ProductoService {

    ProductoResponse crearProducto(ProductoRequest request);

    ProductoResponse actualizarProducto(Long idProducto, ProductoRequest request);

    void cambiarEstadoProducto(Long idProducto, boolean activo);

    List<ProductoResponse> listarProductosActivos();

    List<ProductoResponse> listarProductosInactivos();

    List<ProductoResponse> listarTodosProductos();

    List<ProductoResponse> listarPorCategoria(Long idCategoria);

    ProductoResponse obtenerPorId(Long idProducto);
}

