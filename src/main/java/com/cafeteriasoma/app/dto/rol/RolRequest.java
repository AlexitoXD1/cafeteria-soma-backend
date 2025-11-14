package com.cafeteriasoma.app.dto.rol;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RolRequest {
    private String nombre;
    private String descripcion;
    private Boolean activo;
}
