package com.cafeteriasoma.app.security.auth;

import com.cafeteriasoma.app.entity.Rol;
import com.cafeteriasoma.app.entity.Token;
import com.cafeteriasoma.app.entity.TokenType;
import com.cafeteriasoma.app.entity.Usuario;
import com.cafeteriasoma.app.repository.RolRepository;
import com.cafeteriasoma.app.repository.TokenRepository;
import com.cafeteriasoma.app.repository.UsuarioRepository;
import com.cafeteriasoma.app.security.jwt.JwtService;
import com.cafeteriasoma.app.service.interfaces.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final VerificationCodeService verificationCodeService;

    /**
     * Registro de nuevos usuarios CLIENTE.
     * Genera el código de verificación y lo envía al correo.
     */
    public AuthResponse register(RegisterRequest request) {
        // Verifica si el correo ya está registrado
        usuarioRepository.findByCorreo(request.getCorreo().toLowerCase()).ifPresent(usuarioExistente -> {
        if (!usuarioExistente.getActivo()) {
                // reenviar código
                throw new IllegalArgumentException("Ya existe una cuenta pendiente de verificación. Revisa tu correo o solicita un nuevo codigo.");
        } else {
                throw new IllegalArgumentException("El correo ya se encuentra registrado. Inicia sesion.");
        }
        });
        // Validaciones básicas
        if (!request.getCorreo().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
        throw new IllegalArgumentException("El formato del correo electronico no es valido.");
        }
        if (!request.getTelefono().matches("^[0-9]{9}$")) {
        throw new IllegalArgumentException("El número de telefono debe tener 9 dígitos.");
        }
        // Validación del nombre
        if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
        throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (!request.getNombre().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
        throw new IllegalArgumentException("El nombre solo debe contener letras y espacios.");
        }
        // Validación de la contraseña
        if (!request.getContrasena().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&.])[A-Za-z\\d@$!%*?&.]{8,}$")) {
        throw new IllegalArgumentException(
                "La contraseña debe tener al menos 8 caracteres, una mayuscula, una minuscula, un numero y un simbolo."
        );
        }

        Rol rolCliente = rolRepository.findByNombre("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .correo(request.getCorreo())
                .contrasena(passwordEncoder.encode(request.getContrasena()))
                .telefono(request.getTelefono())
                .rol(rolCliente)
                .activo(false) // <--- El usuario se crea como NO verificado
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Genera el código y envía el correo
        verificationCodeService.generateCode(usuarioGuardado);

        return AuthResponse.builder()
                .message("Usuario registrado exitosamente. Se ha enviado un código de verificación a su correo.")
                .build();
    }

    /**
     * Autenticación de usuario: requiere que esté verificado.
     */
    public AuthResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getCorreo(),
                        request.getContrasena()
                )
        );

        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getActivo()) {
            throw new RuntimeException("Tu cuenta aún no ha sido verificada. Revisa tu correo electrónico.");
        }

        String jwtToken = jwtService.generateToken(usuario);
        revokeAllUserTokens(usuario);
        saveUserToken(usuario, jwtToken);

        return AuthResponse.builder()
                .token(jwtToken)
                .message("Autenticación exitosa")
                .build();
    }

    private void saveUserToken(Usuario usuario, String jwtToken) {
        Token token = Token.builder()
                .usuario(usuario)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Usuario usuario) {
        var validTokens = tokenRepository.findAllValidTokensByUsuario(usuario);
        validTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validTokens);
    }
}
