package com.cafeteriasoma.app.service.interfaces;

import java.util.List;

import com.cafeteriasoma.app.dto.venta.VentaRequest;
import com.cafeteriasoma.app.dto.venta.VentaResponse;

public interface VentaService {

    VentaResponse crearVenta(String correo, VentaRequest request);

    List<VentaResponse> listarMisVentas(String correo);

    List<VentaResponse> listarTodas();
}
