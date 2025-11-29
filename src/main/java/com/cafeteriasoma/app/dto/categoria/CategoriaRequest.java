package com.cafeteriasoma.app.dto.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaRequest {

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    @Size(max = 60, message = "El nombre no debe superar los 60 caracteres")
    private String nombre;

    @Size(max = 200, message = "La descripción no debe superar los 200 caracteres")
    private String descripcion;
}
