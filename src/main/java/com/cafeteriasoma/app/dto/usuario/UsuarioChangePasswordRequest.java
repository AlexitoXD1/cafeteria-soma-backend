package com.cafeteriasoma.app.dto.usuario;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UsuarioChangePasswordRequest {
    private String contrasenaActual;
    private String contrasenaNueva;
}

