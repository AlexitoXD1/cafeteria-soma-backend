package com.cafeteriasoma.app.service.interfaces;

import java.util.List;

import com.cafeteriasoma.app.dto.carrito.CarritoItemRequest;
import com.cafeteriasoma.app.dto.carrito.CarritoItemResponse;

public interface CarritoService {

    List<CarritoItemResponse> obtenerCarrito(String correo);

    CarritoItemResponse agregarAlCarrito(String correo, CarritoItemRequest request);

    CarritoItemResponse actualizarCantidad(String correo, CarritoItemRequest request);

    void eliminarItem(String correo, Long idProducto);

    void vaciarCarrito(String correo);
}
