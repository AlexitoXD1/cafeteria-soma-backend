package com.cafeteriasoma.app.security.userdetails;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cafeteriasoma.app.entity.Usuario;
import com.cafeteriasoma.app.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

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
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(usuario.getRol().getNombre())))
                .build();
    }
}
