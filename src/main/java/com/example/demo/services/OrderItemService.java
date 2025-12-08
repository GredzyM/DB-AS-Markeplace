package com.example.demo.services;

import com.example.demo.dtos.OrderItem.OrderItemRequest;
import com.example.demo.dtos.OrderItem.OrderItemResponse;

import java.util.List;

/**
 * 🛍️ OrderItemService
 * -----------------------------------------------------
 * Define las operaciones de negocio sobre la entidad OrderItem.
 */
public interface OrderItemService {
    OrderItemResponse create(OrderItemRequest request);
    OrderItemResponse update(Long id, OrderItemRequest request);
    void delete(Long id);

    OrderItemResponse findById(Long id);
    List<OrderItemResponse> findAll();
    List<OrderItemResponse> findByOrderId(Long orderId);
}
