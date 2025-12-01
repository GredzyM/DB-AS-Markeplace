package com.example.demo.entitites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Store Entity
 * -----------------------------------------------------
 * ✔ Representa una tienda del marketplace
 * ✔ Pertenece a un usuario (vendedor)
 * ✔ Puede ofrecer varios productos
 */
@Entity
@Table(name = "stores")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la tienda es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String name;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    @Column(length = 500)
    private String description;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    // ✅ Relación: Una tienda pertenece a un usuario
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ✅ Relación: Una tienda puede tener varios productos
    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    private List<Product> products;
}
