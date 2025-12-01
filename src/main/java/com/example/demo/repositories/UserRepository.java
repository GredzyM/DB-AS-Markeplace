package com.example.demo.repositories;

import com.example.demo.entitites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository
 * -----------------------------------------------------
 * ✔ Repositorio JPA para entidad User
 * ✔ Incluye método para buscar por email
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
