package com.example.demo.repositories;

import com.example.demo.entitites.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * OrderRepository
 * -----------------------------------------------------
 * ✔ Repositorio JPA para entidad Order
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    // ✅ Útil para integridad: no borrar usuario si ya tiene pedidos
    boolean existsByUserId(Long userId);
}
