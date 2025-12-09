package com.cafeteriasoma.app.controller.publico;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cafeteriasoma.app.dto.carrito.CarritoItemRequest;
import com.cafeteriasoma.app.dto.carrito.CarritoItemResponse;
import com.cafeteriasoma.app.service.interfaces.CarritoService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador público del carrito de compras.
 * Soporta usuarios autenticados y anónimos mediante un identificador de sesión.
 */
@RestController
@RequestMapping("/api/public/carrito")
@RequiredArgsConstructor
public class CarritoPublicController {

    private final CarritoService carritoService;

    /**
     * Obtiene el identificador del usuario (correo si está autenticado, sessionId si es anónimo).
     * Si es anónimo y no tiene sessionId, genera uno nuevo y lo envía como cookie.
     */
    private String obtenerIdentificadorCarrito(
            @CookieValue(value = "cartId", required = false) String cartId,
            HttpServletResponse response
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Si está autenticado, usa el correo
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return auth.getName(); // correo del usuario
        }

        // Usuario anónimo: usa o genera cartId
        if (cartId == null || cartId.isBlank()) {
            cartId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("cartId", cartId);
            cookie.setPath("/");
            cookie.setMaxAge(7 * 24 * 60 * 60); // 7 días
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        }

        return cartId;
    }

    @GetMapping
    public ResponseEntity<List<CarritoItemResponse>> obtenerCarrito(
            @CookieValue(value = "cartId", required = false) String cartId,
            HttpServletResponse response
    ) {
        String identificador = obtenerIdentificadorCarrito(cartId, response);
        return ResponseEntity.ok(carritoService.obtenerCarrito(identificador));
    }

    @PostMapping
    public ResponseEntity<CarritoItemResponse> agregar(
            @Valid @RequestBody CarritoItemRequest request,
            @CookieValue(value = "cartId", required = false) String cartId,
            HttpServletResponse response
    ) {
        String identificador = obtenerIdentificadorCarrito(cartId, response);
        return ResponseEntity.ok(carritoService.agregarAlCarrito(identificador, request));
    }

    @PutMapping
    public ResponseEntity<CarritoItemResponse> actualizar(
            @Valid @RequestBody CarritoItemRequest request,
            @CookieValue(value = "cartId", required = false) String cartId,
            HttpServletResponse response
    ) {
        String identificador = obtenerIdentificadorCarrito(cartId, response);
        return ResponseEntity.ok(carritoService.actualizarCantidad(identificador, request));
    }

    @DeleteMapping("/{idProducto}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long idProducto,
            @CookieValue(value = "cartId", required = false) String cartId,
            HttpServletResponse response
    ) {
        String identificador = obtenerIdentificadorCarrito(cartId, response);
        carritoService.eliminarItem(identificador, idProducto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> vaciar(
            @CookieValue(value = "cartId", required = false) String cartId,
            HttpServletResponse response
    ) {
        String identificador = obtenerIdentificadorCarrito(cartId, response);
        carritoService.vaciarCarrito(identificador);
        return ResponseEntity.noContent().build();
    }
}
