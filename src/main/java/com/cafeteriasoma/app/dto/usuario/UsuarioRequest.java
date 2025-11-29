package com.cafeteriasoma.app.dto.usuario;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UsuarioRequest {
    private String nombre;
    private String correo;
    private String telefono;
    private String contrasena;
    private Long idRol;
}
