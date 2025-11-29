package com.cafeteriasoma.app.security.userdetails;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cafeteriasoma.app.entity.Usuario;
import com.cafeteriasoma.app.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

/**
 * Servicio personalizado que adapta la entidad Usuario
 * al modelo de autenticación de Spring Security (UserDetails).
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el correo: " + username));

        return User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getContrasena())
                .authorities(usuario.getRol().getNombre()) // ejemplo: "ADMIN" o "CLIENTE"
                .build();
    }
}
