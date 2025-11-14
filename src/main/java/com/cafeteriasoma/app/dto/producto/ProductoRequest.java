package com.cafeteriasoma.app.dto.producto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa los datos requeridos para crear o actualizar un producto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoRequest {

    @NotBlank(message = "El nombre del producto es obligatorio.")
    @Size(max = 120, message = "El nombre no puede exceder los 120 caracteres.")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres.")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio.")
    @Positive(message = "El precio debe ser mayor que 0.")
    private BigDecimal precio;

    @NotNull(message = "El stock es obligatorio.")
    @Min(value = 0, message = "El stock no puede ser negativo.")
    private Integer stock;

    @NotNull(message = "Debe especificarse una categoría válida.")
    private Long idCategoria;

    @Size(max = 512, message = "La URL de la imagen no puede exceder los 512 caracteres.")
    private String imagenUrl;
}
