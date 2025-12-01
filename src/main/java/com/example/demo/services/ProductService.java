package com.example.demo.services;

import com.example.demo.dtos.Product.ProductRequest;
import com.example.demo.dtos.Product.ProductResponse;

import java.util.List;

/**
 * 📦 ProductService
 * -----------------------------------------------------
 * Define las operaciones de negocio sobre la entidad Product.
 */
public interface ProductService {
    ProductResponse create(ProductRequest request);
    ProductResponse update(Long id, ProductRequest request);
    void delete(Long id);

    ProductResponse findById(Long id);
    List<ProductResponse> findAll();
    List<ProductResponse> findByStoreId(Long storeId);
}
