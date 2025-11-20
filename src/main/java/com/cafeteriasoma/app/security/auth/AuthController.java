package com.cafeteriasoma.app.security.auth;

import com.cafeteriasoma.app.service.interfaces.VerificationCodeService;

import jakarta.servlet.http.HttpServletRequest;

import com.cafeteriasoma.app.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import com.cafeteriasoma.app.entity.Usuario;

/**
 * Controlador de autenticación y registro de usuarios.
 * Incluye verificación de cuenta por correo electrónico.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final VerificationCodeService verificationCodeService;
    private final UsuarioRepository usuarioRepository; // <-- inyectado

    /**
     * Registro de nuevos usuarios (CLIENTE por defecto).
     * Envia un código de verificación por correo electrónico.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    /**
     * Inicio de sesión de usuario. Requiere cuenta verificada.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    /**
     * Verificación de cuenta a través del código enviado al correo.
     */
    @PostMapping("/verify")
    public ResponseEntity<String> verifyAccount(
            @RequestParam String correo,
            @RequestParam String codigo
    ) {
        boolean verified = verificationCodeService.verifyCode(correo, codigo);

        if (verified) {
            return ResponseEntity.ok("Cuenta verificada correctamente. Ya puedes iniciar sesión.");
        } else {
            return ResponseEntity.badRequest()
                    .body("Código invalido, expirado o usuario no encontrado.");
        }
    }
    
    /**
     * Reenvía el código de verificación a un usuario que no ha verificado su cuenta.
     */
    @PostMapping("/resend-code")
    public ResponseEntity<String> resendCode(@RequestParam String correo) {
        var usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("No existe una cuenta asociada a este correo.");
        }
        Usuario usuario = usuarioOpt.get();
        if (Boolean.TRUE.equals(usuario.getActivo())) {
            return ResponseEntity.badRequest().body("La cuenta ya fue verificada.");
        }

        try {
            verificationCodeService.generateCode(usuario);
            return ResponseEntity.ok("Se ha reenviado el código de verificación a tu correo.");
        } catch (Exception e) {
            // Loguear el error si tienes logger; devolver mensaje genérico al cliente
            return ResponseEntity.status(500).body("Error al reenviar el código. Intenta más tarde.");
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        
        // 1. Obtener el JWT del Header de la solicitud
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Este caso en teoría no debería pasar si el endpoint está protegido por el filtro JWT,
            // pero es una buena práctica de seguridad.
            return ResponseEntity.status(401).body("Acceso no autorizado.");
        }
        final String jwtToken = authHeader.substring(7);

        // 2. Obtener la información de autenticación del contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Usuario)) {
            return ResponseEntity.status(401).body("Usuario no autenticado o no reconocido.");
        }

        Usuario usuario = (Usuario) authentication.getPrincipal();

        // 3. Llamar al servicio para revocar el token
        authenticationService.logout(usuario, jwtToken);

        // 4. Limpiar el contexto de seguridad (opcional, pero buena práctica)
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("Sesión cerrada exitosamente.");
    }

}
