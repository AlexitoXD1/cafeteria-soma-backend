package com.cafeteriasoma.app.dto.usuario;

import java.time.LocalDateTime;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {
    private Long idUsuario;
    private String nombre;
    private String correo;
    private String telefono;
    private String rol;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}
