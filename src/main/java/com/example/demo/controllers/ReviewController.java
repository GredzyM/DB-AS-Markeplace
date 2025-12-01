package com.example.demo.controllers;

import com.example.demo.common.ApiResponse;
import com.example.demo.dtos.Review.ReviewRequest;
import com.example.demo.dtos.Review.ReviewResponse;
import com.example.demo.services.ReviewService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ReviewController
 * -----------------------------------------------------
 * ✔ CRUD completo para Review
 */
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Validated
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.ok("Reseñas obtenidas exitosamente", reviewService.findAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getById(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok("Reseña encontrada", reviewService.findById(id))
        );
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getByProductId(
            @PathVariable @Positive(message = "El ID del producto debe ser positivo") Long productId
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok("Reseñas del producto obtenidas", reviewService.findByProductId(productId))
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getByUserId(
            @PathVariable @Positive(message = "El ID del usuario debe ser positivo") Long userId
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok("Reseñas del usuario obtenidas", reviewService.findByUserId(userId))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> create(@Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.ok("Reseña creada exitosamente", reviewService.create(request))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponse>> update(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id,
            @Valid @RequestBody ReviewRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok("Reseña actualizada exitosamente", reviewService.update(id, request))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        reviewService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Reseña eliminada exitosamente", null));
    }
}
