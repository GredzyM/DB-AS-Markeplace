package com.example.demo.controllers;

import com.example.demo.common.ApiResponse;
import com.example.demo.dtos.Order.OrderRequest;
import com.example.demo.dtos.Order.OrderResponse;
import com.example.demo.entitites.OrderStatus;
import com.example.demo.services.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok("Pedidos obtenidos exitosamente", orderService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getById(@PathVariable @Positive(message = "El ID debe ser positivo") Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Pedido encontrado", orderService.findById(id)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getByUserId(@PathVariable @Positive(message = "El ID de usuario debe ser positivo") Long userId) {
        return ResponseEntity.ok(ApiResponse.ok("Pedidos del usuario obtenidos", orderService.findByUserId(userId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> create(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Pedido creado exitosamente", orderService.create(request)));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateStatus(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id,
            @RequestParam @NotNull(message = "El estado es obligatorio") OrderStatus status
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok("Estado del pedido actualizado", orderService.updateStatus(id, status.name()))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable @Positive(message = "El ID debe ser positivo") Long id) {
        orderService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Pedido eliminado exitosamente", null));
    }
}
