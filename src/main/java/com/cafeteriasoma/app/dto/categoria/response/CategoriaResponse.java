package com.cafeteriasoma.app.dto.categoria.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CategoriaResponse {
    Long idCategoria;
    String nombre;
    String descripcion;
    Boolean activo;
    LocalDateTime fechaCreacion;
    LocalDateTime fechaActualizacion;
}
