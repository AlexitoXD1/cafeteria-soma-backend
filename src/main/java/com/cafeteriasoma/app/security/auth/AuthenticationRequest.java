package com.cafeteriasoma.app.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa la solicitud de inicio de sesión (login).
 * Recibe el correo y la contraseña enviados por el cliente.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    private String correo;
    private String contrasena;
}
