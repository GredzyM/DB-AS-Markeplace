package com.example.demo.services;

import com.example.demo.dtos.Order.OrderRequest;
import com.example.demo.dtos.Order.OrderResponse;

import java.util.List;

/**
 * 🛒 OrderService
 * -----------------------------------------------------
 * Define las operaciones de negocio sobre la entidad Order.
 */
public interface OrderService {
    OrderResponse create(OrderRequest request);
    OrderResponse updateStatus(Long id, String status);
    void delete(Long id);

    OrderResponse findById(Long id);
    List<OrderResponse> findAll();
    List<OrderResponse> findByUserId(Long userId);
}
