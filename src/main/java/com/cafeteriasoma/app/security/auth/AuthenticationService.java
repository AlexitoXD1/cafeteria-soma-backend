package com.cafeteriasoma.app.security.auth;

import com.cafeteriasoma.app.entity.Rol;
import com.cafeteriasoma.app.entity.Token;
import com.cafeteriasoma.app.entity.TokenType;
import com.cafeteriasoma.app.entity.Usuario;
import com.cafeteriasoma.app.repository.RolRepository;
import com.cafeteriasoma.app.repository.TokenRepository;
import com.cafeteriasoma.app.repository.UsuarioRepository;
import com.cafeteriasoma.app.security.jwt.JwtService;
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

    public AuthResponse register(RegisterRequest request) {
        Rol rolCliente = rolRepository.findByNombre("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado"));

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .correo(request.getCorreo())
                .contrasena(passwordEncoder.encode(request.getContrasena()))
                .telefono(request.getTelefono())
                .rol(rolCliente)
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        String jwtToken = jwtService.generateToken(usuarioGuardado);

        saveUserToken(usuarioGuardado, jwtToken);

        return AuthResponse.builder()
                .token(jwtToken)
                .message("Usuario registrado exitosamente")
                .build();
    }

    public AuthResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getCorreo(),
                        request.getContrasena()
                )
        );

        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

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
