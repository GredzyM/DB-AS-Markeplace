package com.example.demo.services;

import com.example.demo.dtos.Store.StoreRequest;
import com.example.demo.dtos.Store.StoreResponse;

import java.util.List;

/**
 * 🏪 StoreService
 * -----------------------------------------------------
 * Define las operaciones de negocio sobre la entidad Store.
 */
public interface StoreService {
    StoreResponse create(StoreRequest request);
    StoreResponse update(Long id, StoreRequest request);
    void delete(Long id);

    StoreResponse findById(Long id);
    List<StoreResponse> findAll();
    List<StoreResponse> findByUserId(Long userId);
}
