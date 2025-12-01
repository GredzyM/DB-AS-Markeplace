package com.example.demo.services;

import com.example.demo.dtos.User.UserRequest;
import com.example.demo.dtos.User.UserResponse;

import java.util.List;

/**
 * 👤 UserService
 * -----------------------------------------------------
 * Define las operaciones de negocio sobre la entidad User.
 */
public interface UserService {
    UserResponse create(UserRequest request);
    UserResponse update(Long id, UserRequest request);
    void delete(Long id);

    UserResponse findById(Long id);
    List<UserResponse> findAll();
}
