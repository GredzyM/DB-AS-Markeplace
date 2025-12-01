package com.example.demo.repositories;

import com.example.demo.entitites.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * OrderItemRepository
 * -----------------------------------------------------
 * ✔ Repositorio JPA para entidad OrderItem
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);

    // ✅ Útil para reglas de integridad: no borrar producto si ya fue vendido
    boolean existsByProductId(Long productId);

    // ✅ Útil para reglas de integridad: validar si un pedido tiene items
    boolean existsByOrderId(Long orderId);
}
