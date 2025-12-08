package com.example.demo.dtos.User;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String role;
    // ✅ NO incluimos password por seguridad
}
