package com.example.demo.entitites;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * User Entity
 * -----------------------------------------------------
 * ✔ Representa un usuario del sistema
 * ✔ Implementa UserDetails para integración con Spring Security
 * ✔ Un usuario puede tener tiendas, hacer pedidos y escribir reseñas
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String firstname;

    @Column(nullable = false, length = 50)
    private String lastname;

    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String role = "ROLE_USER";

    // ✅ Relación: Un usuario puede tener varias tiendas
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Store> stores;

    // ✅ Relación: Un usuario puede hacer varios pedidos
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders;

    // ✅ Relación: Un usuario puede escribir varias reseñas
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Review> reviews;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String effectiveRole = (role != null && !role.isBlank()) ? role : "ROLE_USER";
        return List.of(new SimpleGrantedAuthority(effectiveRole));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
