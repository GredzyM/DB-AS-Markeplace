package com.example.demo.dtos.Review;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {

    private Long id;
    private Integer rating;
    private String comment;
    private Date reviewDate;
    private Long userId;
    private String userName; // Nombre del usuario que escribió la reseña
    private Long productId;
    private String productName; // Nombre del producto
}
