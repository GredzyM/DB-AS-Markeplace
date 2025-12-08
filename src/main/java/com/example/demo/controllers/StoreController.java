package com.example.demo.controllers;

import com.example.demo.common.ApiResponse;
import com.example.demo.dtos.Store.StoreRequest;
import com.example.demo.dtos.Store.StoreResponse;
import com.example.demo.services.StoreService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * StoreController
 * -----------------------------------------------------
 * ✔ CRUD completo para Store
 */
@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
@Validated
public class StoreController {

    private final StoreService storeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StoreResponse>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.ok("Tiendas obtenidas exitosamente", storeService.findAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StoreResponse>> getById(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok("Tienda encontrada", storeService.findById(id))
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<StoreResponse>>> getByUserId(
            @PathVariable @Positive(message = "El ID del usuario debe ser positivo") Long userId
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok("Tiendas del usuario obtenidas", storeService.findByUserId(userId))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StoreResponse>> create(@Valid @RequestBody StoreRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.ok("Tienda creada exitosamente", storeService.create(request))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StoreResponse>> update(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id,
            @Valid @RequestBody StoreRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok("Tienda actualizada exitosamente", storeService.update(id, request))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        storeService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Tienda eliminada exitosamente", null));
    }
}
