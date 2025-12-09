package com.cafeteriasoma.app.service.impl;

import com.cafeteriasoma.app.dto.promocion.PromocionMapper;
import com.cafeteriasoma.app.dto.promocion.PromocionRequest;
import com.cafeteriasoma.app.dto.promocion.PromocionResponse;
import com.cafeteriasoma.app.entity.Producto;
import com.cafeteriasoma.app.entity.Promocion;
import com.cafeteriasoma.app.exception.BadRequestException;
import com.cafeteriasoma.app.exception.ResourceNotFoundException;
import com.cafeteriasoma.app.repository.ProductoRepository;
import com.cafeteriasoma.app.repository.PromocionRepository;
import com.cafeteriasoma.app.service.interfaces.PromocionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PromocionServiceImpl implements PromocionService {

    private final PromocionRepository promocionRepository;
    private final ProductoRepository productoRepository;

    @Override
    public PromocionResponse crearPromocion(PromocionRequest request) {
        validarFechas(request);

        Promocion promocion = PromocionMapper.toEntity(request);
        
        // Asignar productos si vienen en el request
        if (request.getIdsProductos() != null && !request.getIdsProductos().isEmpty()) {
            Set<Producto> productos = buscarProductos(request.getIdsProductos());
            promocion.setProductos(productos);
        }

        return PromocionMapper.toResponse(promocionRepository.save(promocion));
    }

    @Override
    public PromocionResponse actualizarPromocion(Long id, PromocionRequest request) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promoción no encontrada"));

        validarFechas(request);

        PromocionMapper.updateEntity(promocion, request);

        // Actualizar la lista de productos (Sobrescribe la lista anterior)
        if (request.getIdsProductos() != null) {
            Set<Producto> productos = buscarProductos(request.getIdsProductos());
            promocion.setProductos(productos);
        }

        return PromocionMapper.toResponse(promocionRepository.save(promocion));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromocionResponse> listarTodas() {
        return promocionRepository.findAll().stream()
                .map(PromocionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PromocionResponse obtenerPorId(Long id) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promoción no encontrada"));
        return PromocionMapper.toResponse(promocion);
    }

    @Override
    public void cambiarEstado(Long id, boolean activo) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Promoción no encontrada"));
        promocion.setActivo(activo);
        promocionRepository.save(promocion);
    }

    @Override
    public void eliminarPromocion(Long id) {
        if (!promocionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Promoción no encontrada");
        }
        promocionRepository.deleteById(id);
    }

    // ================= Helpers =================

    private void validarFechas(PromocionRequest request) {
        if (request.getFechaInicio().isAfter(request.getFechaFin())) {
            throw new BadRequestException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }
    }

    private Set<Producto> buscarProductos(List<Long> ids) {
        List<Producto> productosEncontrados = productoRepository.findAllById(ids);
        
        if (productosEncontrados.size() != ids.size()) {
            throw new ResourceNotFoundException("Uno o más productos especificados no existen.");
        }
        
        return new HashSet<>(productosEncontrados);
    }
}