package com.example.demo.controllers;

import com.example.demo.common.ApiResponse;
import com.example.demo.dtos.OrderItem.OrderItemRequest;
import com.example.demo.dtos.OrderItem.OrderItemResponse;
import com.example.demo.services.OrderItemService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * OrderItemController
 * -----------------------------------------------------
 * ✔ CRUD completo para OrderItem
 */
@RestController
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
@Validated
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderItemResponse>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.ok("Items obtenidos exitosamente", orderItemService.findAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderItemResponse>> getById(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok("Item encontrado", orderItemService.findById(id))
        );
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<List<OrderItemResponse>>> getByOrderId(
            @PathVariable @Positive(message = "El ID del pedido debe ser positivo") Long orderId
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok("Items del pedido obtenidos", orderItemService.findByOrderId(orderId))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderItemResponse>> create(@Valid @RequestBody OrderItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.ok("Item creado exitosamente", orderItemService.create(request))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderItemResponse>> update(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id,
            @Valid @RequestBody OrderItemRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok("Item actualizado exitosamente", orderItemService.update(id, request))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        orderItemService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Item eliminado exitosamente", null));
    }
}
