package com.example.demo.repositories;

import com.example.demo.entitites.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ReviewRepository
 * -----------------------------------------------------
 * ✔ Repositorio JPA para entidad Review
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProductId(Long productId);

    List<Review> findByUserId(Long userId);

    // ✅ Evitar doble reseña por usuario-producto (además del unique constraint)
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    boolean existsByUserId(Long userId);
}
