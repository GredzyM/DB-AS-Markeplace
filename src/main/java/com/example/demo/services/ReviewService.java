package com.example.demo.services;

import com.example.demo.dtos.Review.ReviewRequest;
import com.example.demo.dtos.Review.ReviewResponse;

import java.util.List;

/**
 * ⭐ ReviewService
 * -----------------------------------------------------
 * Define las operaciones de negocio sobre la entidad Review.
 */
public interface ReviewService {
    ReviewResponse create(ReviewRequest request);
    ReviewResponse update(Long id, ReviewRequest request);
    void delete(Long id);

    ReviewResponse findById(Long id);
    List<ReviewResponse> findAll();
    List<ReviewResponse> findByProductId(Long productId);
    List<ReviewResponse> findByUserId(Long userId);
}
