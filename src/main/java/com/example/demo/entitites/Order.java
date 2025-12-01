package com.example.demo.entitites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Order Entity
 * -----------------------------------------------------
 * ✔ Representa un pedido realizado por un usuario
 * ✔ Pertenece a un usuario (comprador)
 * ✔ Contiene varios items de productos
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OrderStatus status = OrderStatus.PENDING;

    // ✅ Relación: Un pedido pertenece a un usuario
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ✅ Relación: Un pedido contiene varios items
    @OneToMany(
            mappedBy = "order",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> orderItems = new ArrayList<>();

    @PrePersist
    void onCreate() {
        if (orderDate == null) orderDate = new Date();
        if (status == null) status = OrderStatus.PENDING;
        if (total == null) total = BigDecimal.ZERO;

    }

    /**
     * Helper para mantener la relación bidireccional consistente.
     */
    public void addOrderItem(OrderItem item) {
        if (item == null) return;
        orderItems.add(item);
        item.setOrder(this);
    }

    public void removeOrderItem(OrderItem item) {
        if (item == null) return;
        orderItems.remove(item);
        item.setOrder(null);
    }
}
