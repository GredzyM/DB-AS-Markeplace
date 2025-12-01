package com.example.demo.dtos.Order;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;
    private Date orderDate;
    private BigDecimal total;
    private String status;
    private Long userId;
    private String userEmail; // Email del comprador
    private List<OrderItemSummary> items; // Lista de items del pedido

    /**
     * Resumen de cada item del pedido (salida).
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemSummary {
        private Long id;
        private String productName;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal subtotal;
    }
}
