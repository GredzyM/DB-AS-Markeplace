package com.example.demo.dtos.Store;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreResponse {

    private Long id;
    private String name;
    private String description;
    private Boolean active;
    private Long userId; // ID del propietario
    private String userEmail; // Email del propietario
}
