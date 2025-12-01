package com.example.demo.servicesImpl;

import com.example.demo.dtos.auth.AuthenticationRequest;
import com.example.demo.dtos.auth.AuthenticationResponse;
import com.example.demo.dtos.auth.RegisterRequest;
import com.example.demo.entitites.User;
import com.example.demo.exceptions.BusinessRuleException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.JwtService;
import com.example.demo.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * AuthServiceImpl
 * -----------------------------------------------------
 * ✔ Gestiona registro y autenticación de usuarios
 * ✔ Genera tokens JWT válidos
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {

        String email = request.getEmail() == null ? null : request.getEmail().trim().toLowerCase();

        if (email == null || email.isBlank()) {
            throw new BusinessRuleException("El correo es obligatorio");
        }

        if (userRepository.existsByEmail(email)) {
            throw new BusinessRuleException("Ya existe un usuario con el email: " + email);
        }

        User user = new User();
        user.setFirstname(request.getFirstname().trim());
        user.setLastname(request.getLastname().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_USER");

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(user.getId())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .role(user.getRole())
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        String email = request.getEmail() == null ? null : request.getEmail().trim().toLowerCase();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(user.getId())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .role(user.getRole())
                .build();
    }
}
