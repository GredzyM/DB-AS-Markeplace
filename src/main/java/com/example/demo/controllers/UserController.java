package com.example.demo.controllers;

import com.example.demo.common.ApiResponse;
import com.example.demo.dtos.User.UserRequest;
import com.example.demo.dtos.User.UserResponse;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UserController
 * -----------------------------------------------------
 * ✔ CRUD completo para User
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.ok("Usuarios obtenidos exitosamente", userService.findAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getById(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok("Usuario encontrado", userService.findById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> create(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.ok("Usuario creado exitosamente", userService.create(request))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> update(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id,
            @Valid @RequestBody UserRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok("Usuario actualizado exitosamente", userService.update(id, request))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ) {
        userService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Usuario eliminado exitosamente", null));
    }
}
