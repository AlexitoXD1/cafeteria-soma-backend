package com.cafeteriasoma.app.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cafeteriasoma.app.dto.carrito.CarritoItemRequest;
import com.cafeteriasoma.app.dto.carrito.CarritoItemResponse;
import com.cafeteriasoma.app.dto.carrito.CarritoMapper;
import com.cafeteriasoma.app.entity.CarritoTemporal;
import com.cafeteriasoma.app.entity.Producto;
import com.cafeteriasoma.app.entity.Usuario;
import com.cafeteriasoma.app.exception.BadRequestException;
import com.cafeteriasoma.app.exception.ResourceNotFoundException;
import com.cafeteriasoma.app.repository.CarritoTemporalRepository;
import com.cafeteriasoma.app.repository.ProductoRepository;
import com.cafeteriasoma.app.repository.UsuarioRepository;
import com.cafeteriasoma.app.service.interfaces.CarritoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CarritoServiceImpl implements CarritoService {

    private final CarritoTemporalRepository carritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CarritoItemResponse> obtenerCarrito(String identificador) {
        if (esCorreo(identificador)) {
            Usuario usuario = obtenerUsuario(identificador);
            return carritoRepository.findByUsuario(usuario).stream()
                    .map(CarritoMapper::toResponse)
                    .toList();
        } else {
            return carritoRepository.findBySessionId(identificador).stream()
                    .map(CarritoMapper::toResponse)
                    .toList();
        }
    }

    @Override
    public CarritoItemResponse agregarAlCarrito(String identificador, CarritoItemRequest request) {
        if (request.getCantidad() == null || request.getCantidad() < 1) {
            throw new BadRequestException("La cantidad debe ser al menos 1");
        }

        Producto producto = obtenerProductoActivo(request.getIdProducto());

        if (producto.getStock() < request.getCantidad()) {
            throw new BadRequestException("No hay stock suficiente para este producto");
        }

        CarritoTemporal carrito;

        if (esCorreo(identificador)) {
            Usuario usuario = obtenerUsuario(identificador);
            carrito = carritoRepository.findByUsuarioAndProducto(usuario, producto)
                    .orElseGet(() -> CarritoTemporal.builder()
                            .usuario(usuario)
                            .producto(producto)
                            .cantidad(0)
                            .build()
                    );
        } else {
            carrito = carritoRepository.findBySessionIdAndProducto(identificador, producto)
                    .orElseGet(() -> CarritoTemporal.builder()
                            .sessionId(identificador)
                            .producto(producto)
                            .cantidad(0)
                            .build()
                    );
        }

        int nuevaCantidad = carrito.getCantidad() + request.getCantidad();

        if (nuevaCantidad > producto.getStock()) {
            throw new BadRequestException("La cantidad total en el carrito supera el stock disponible");
        }

        carrito.setCantidad(nuevaCantidad);
        CarritoTemporal guardado = carritoRepository.save(carrito);

        return CarritoMapper.toResponse(guardado);
    }

    @Override
    public CarritoItemResponse actualizarCantidad(String identificador, CarritoItemRequest request) {
        if (request.getCantidad() == null || request.getCantidad() < 1) {
            throw new BadRequestException("La cantidad debe ser al menos 1");
        }

        Producto producto = obtenerProductoActivo(request.getIdProducto());

        CarritoTemporal carrito;

        if (esCorreo(identificador)) {
            Usuario usuario = obtenerUsuario(identificador);
            carrito = carritoRepository.findByUsuarioAndProducto(usuario, producto)
                    .orElseThrow(() -> new ResourceNotFoundException("El producto no está en el carrito"));
        } else {
            carrito = carritoRepository.findBySessionIdAndProducto(identificador, producto)
                    .orElseThrow(() -> new ResourceNotFoundException("El producto no está en el carrito"));
        }

        if (request.getCantidad() > producto.getStock()) {
            throw new BadRequestException("La cantidad solicitada supera el stock disponible");
        }

        carrito.setCantidad(request.getCantidad());
        CarritoTemporal guardado = carritoRepository.save(carrito);

        return CarritoMapper.toResponse(guardado);
    }

    @Override
    public void eliminarItem(String identificador, Long idProducto) {
        Producto producto = obtenerProductoActivo(idProducto);

        CarritoTemporal carrito;

        if (esCorreo(identificador)) {
            Usuario usuario = obtenerUsuario(identificador);
            carrito = carritoRepository.findByUsuarioAndProducto(usuario, producto)
                    .orElseThrow(() -> new ResourceNotFoundException("El producto no está en el carrito"));
        } else {
            carrito = carritoRepository.findBySessionIdAndProducto(identificador, producto)
                    .orElseThrow(() -> new ResourceNotFoundException("El producto no está en el carrito"));
        }

        carritoRepository.delete(carrito);
    }

    @Override
    public void vaciarCarrito(String identificador) {
        if (esCorreo(identificador)) {
            Usuario usuario = obtenerUsuario(identificador);
            carritoRepository.deleteByUsuario(usuario);
        } else {
            carritoRepository.deleteBySessionId(identificador);
        }
    }

    @Override
    public void migrarCarritoAnonimo(String sessionId, String correo) {
        Usuario usuario = obtenerUsuario(correo);
        carritoRepository.migrarCarritoAnonimo(sessionId, usuario);
    }

    // ====================== helpers internos ======================

    private boolean esCorreo(String identificador) {
        return identificador != null && identificador.contains("@");
    }

    private Usuario obtenerUsuario(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    private Producto obtenerProductoActivo(Long idProducto) {
        return productoRepository.findById(idProducto)
                .filter(p -> Boolean.TRUE.equals(p.getActivo()))
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado o inactivo"));
    }
}
