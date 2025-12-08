package com.example.demo.entitites;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Review Entity
 * -----------------------------------------------------
 * ✔ Representa una reseña de un producto
 * ✔ Escrita por un usuario
 * ✔ Corresponde a un producto específico
 */
@Entity
@Table(
        name = "reviews",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"})
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    @Column(nullable = false)
    private Integer rating;

    @Size(max = 1000, message = "El comentario no puede superar los 1000 caracteres")
    @Column(length = 1000)
    private String comment;

    @Column(name = "review_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date reviewDate;

    // ✅ Relación: Una reseña es escrita por un usuario
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ✅ Relación: Una reseña corresponde a un producto
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @PrePersist
    void onCreate() {
        if (reviewDate == null) reviewDate = new Date();
    }
}
