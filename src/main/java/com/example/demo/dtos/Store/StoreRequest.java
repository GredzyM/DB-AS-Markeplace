package com.example.demo.dtos.Store;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreRequest {

    @NotBlank(message = "El nombre de la tienda es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String name;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String description;

    @NotNull(message = "El ID del usuario propietario es obligatorio")
    @Positive(message = "El ID del usuario propietario debe ser un número positivo")
    private Long userId; // ID del usuario propietario de la tienda
}
