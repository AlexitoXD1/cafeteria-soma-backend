package com.cafeteriasoma.app.security.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cafeteriasoma.app.security.userdetails.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Filtro que valida el token JWT en cada petición HTTP entrante.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader("Authorization");
            log.debug("Authorization header: {}", authHeader);
            
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.debug("No Bearer token found, skipping JWT validation");
                filterChain.doFilter(request, response);
                return;
            }

            final String jwt = authHeader.substring(7);
            log.debug("JWT token extracted");
            
            final String userEmail = jwtService.extractUsername(jwt);
            log.debug("Username extracted from JWT: {}", userEmail);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                log.debug("User details loaded: {}, Authorities: {}", userEmail, userDetails.getAuthorities());

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    log.info("Token válido para usuario: {}", userEmail);
                    var authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("Authentication set in SecurityContext");
                } else {
                    log.warn("Token validation failed for user: {}", userEmail);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Error al procesar el token JWT: ", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token invalido");
        }
    }
}
