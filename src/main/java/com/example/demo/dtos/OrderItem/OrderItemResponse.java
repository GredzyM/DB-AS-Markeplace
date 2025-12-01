package com.example.demo.dtos.OrderItem;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {

    private Long id;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
    private Long orderId;
    private Long productId;
    private String productName; // Nombre del producto
}
