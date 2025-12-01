package com.example.demo.controllers;

import com.example.demo.common.ApiResponse;
import com.example.demo.dtos.Product.ProductRequest;
import com.example.demo.dtos.Product.ProductResponse;
import com.example.demo.services.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProductController
 * -----------------------------------------------------
 * ✔ CRUD completo para Product
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.ok("Productos obtenidos exitosamente", productService.findAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getById(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok("Producto encontrado", productService.findById(id))
        );
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getByStoreId(
            @PathVariable @Positive(message = "El ID de la tienda debe ser positivo") Long storeId
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok("Productos de la tienda obtenidos", productService.findByStoreId(storeId))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> create(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.ok("Producto creado exitosamente", productService.create(request))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> update(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id,
            @Valid @RequestBody ProductRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok("Producto actualizado exitosamente", productService.update(id, request))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        productService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Producto eliminado exitosamente", null));
    }
}
