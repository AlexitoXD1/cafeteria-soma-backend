package com.cafeteriasoma.app.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa la solicitud de registro de un nuevo usuario.
 * Separa los datos del formulario de la entidad interna Usuario.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String nombre;
    private String correo;
    private String contrasena;
    private String telefono;
}
