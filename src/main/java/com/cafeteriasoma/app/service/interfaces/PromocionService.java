package com.cafeteriasoma.app.service.interfaces;

import com.cafeteriasoma.app.dto.promocion.PromocionRequest;
import com.cafeteriasoma.app.dto.promocion.PromocionResponse;

import java.util.List;

public interface PromocionService {
    
    PromocionResponse crearPromocion(PromocionRequest request);
    
    PromocionResponse actualizarPromocion(Long id, PromocionRequest request);
    
    List<PromocionResponse> listarTodas();
    
    PromocionResponse obtenerPorId(Long id);
    
    void cambiarEstado(Long id, boolean activo);
    
    void eliminarPromocion(Long id);
}