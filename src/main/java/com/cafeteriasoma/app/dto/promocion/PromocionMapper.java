package com.cafeteriasoma.app.dto.promocion;

import com.cafeteriasoma.app.dto.producto.ProductoMapper;
import com.cafeteriasoma.app.dto.producto.ProductoResponse;
import com.cafeteriasoma.app.entity.Promocion;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PromocionMapper {

    public static Promocion toEntity(PromocionRequest request) {
        return Promocion.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .porcentajeDescuento(request.getPorcentajeDescuento())
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .activo(true) // Por defecto activa al crear
                .build();
    }

    public static void updateEntity(Promocion promocion, PromocionRequest request) {
        promocion.setNombre(request.getNombre());
        promocion.setDescripcion(request.getDescripcion());
        promocion.setPorcentajeDescuento(request.getPorcentajeDescuento());
        promocion.setFechaInicio(request.getFechaInicio());
        promocion.setFechaFin(request.getFechaFin());
    }

    public static PromocionResponse toResponse(Promocion promocion) {
        List<ProductoResponse> productosResponse = (promocion.getProductos() != null)
                ? promocion.getProductos().stream()
                    .map(ProductoMapper::toResponse)
                    .collect(Collectors.toList())
                : Collections.emptyList();

        return PromocionResponse.builder()
                .idPromocion(promocion.getIdPromocion())
                .nombre(promocion.getNombre())
                .descripcion(promocion.getDescripcion())
                .porcentajeDescuento(promocion.getPorcentajeDescuento())
                .fechaInicio(promocion.getFechaInicio())
                .fechaFin(promocion.getFechaFin())
                .activo(promocion.getActivo())
                .productos(productosResponse)
                .fechaCreacion(promocion.getFechaCreacion())
                .fechaActualizacion(promocion.getFechaActualizacion())
                .build();
    }
}