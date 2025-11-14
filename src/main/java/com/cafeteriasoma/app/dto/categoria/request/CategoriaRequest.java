package com.cafeteriasoma.app.dto.categoria.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaRequest {

    @NotBlank(message = "El nombre de la categoría es obligatorio.")
    @Size(max = 60, message = "El nombre de la categoría no debe exceder 60 caracteres.")
    private String nombre;

    @Size(max = 200, message = "La descripción de la categoría no debe exceder 200 caracteres.")
    private String descripcion;
}
