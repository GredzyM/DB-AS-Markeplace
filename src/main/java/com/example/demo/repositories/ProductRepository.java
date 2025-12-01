package com.example.demo.repositories;

import com.example.demo.entitites.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ProductRepository
 * -----------------------------------------------------
 * ✔ Repositorio JPA para entidad Product
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStoreId(Long storeId);

    // ✅ Útil para integridad: saber si una tienda tiene productos
    boolean existsByStoreId(Long storeId);
}
