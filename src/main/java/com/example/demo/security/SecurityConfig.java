package com.example.demo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig
 * -----------------------------------------------------
 * ✔ Configura Spring Security con JWT
 * ✔ Deshabilita CSRF (porque usamos token)
 * ✔ Aplica CORS global desde CorsConfig
 * ✔ Protege rutas excepto /api/auth/**
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Habilitar CORS global (configurado en CorsConfig)
                .cors(cors -> cors.configure(http))
                // Desactivar CSRF (no se usa con JWT)
                .csrf(AbstractHttpConfigurer::disable)
                // Definir rutas públicas y protegidas

                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas (sin autenticación)
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/register"
                        ).permitAll()

                        // Rutas protegidas (requieren autenticación)
                        .requestMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/stores/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/products/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/orders/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/order-items/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/reviews/**").hasAnyRole("USER", "ADMIN")

                        // Ruta de perfil (cualquier usuario autenticado)
                        .requestMatchers("/api/auth/me").authenticated()

                        // Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated()
                )

                // Política de sesión sin estado
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Registrar el AuthenticationProvider
                .authenticationProvider(authenticationProvider)
                // Registrar el filtro JWT antes del UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
