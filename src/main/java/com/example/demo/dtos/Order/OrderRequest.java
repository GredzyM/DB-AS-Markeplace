package com.example.demo.dtos.Order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    @NotNull(message = "El ID del usuario es obligatorio")
    @Positive(message = "El ID del usuario debe ser un número positivo")
    private Long userId;

    @NotEmpty(message = "El pedido debe contener al menos un item")
    @Valid
    private List<OrderItemData> items; // Items del pedido

    /**
     * Item del pedido (entrada).
     * Nota: el backend calcula price/subtotal/total (no deben venir del cliente).
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemData {

        @NotNull(message = "El ID del producto es obligatorio")
        @Positive(message = "El ID del producto debe ser un número positivo")
        private Long productId;

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        private Integer quantity;
    }
}
