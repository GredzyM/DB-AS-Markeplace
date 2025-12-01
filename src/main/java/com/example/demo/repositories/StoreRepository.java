package com.example.demo.repositories;

import com.example.demo.entitites.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * StoreRepository
 * -----------------------------------------------------
 * ✔ Repositorio JPA para entidad Store
 */
@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    List<Store> findByUserId(Long userId);

    // ✅ Útil para integridad: saber si un user tiene tiendas
    boolean existsByUserId(Long userId);
}
