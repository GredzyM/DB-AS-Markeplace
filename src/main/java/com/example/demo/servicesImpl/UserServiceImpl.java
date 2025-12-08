package com.example.demo.servicesImpl;

import com.example.demo.dtos.User.UserRequest;
import com.example.demo.dtos.User.UserResponse;
import com.example.demo.entitites.User;
import com.example.demo.exceptions.BusinessRuleException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mappers.UserMapper;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.ReviewRepository;
import com.example.demo.repositories.StoreRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse create(UserRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        if (repository.existsByEmail(email)) {
            throw new BusinessRuleException("Ya existe un usuario con el email: " + email);
        }

        User entity = mapper.toEntity(request);
        entity.setEmail(email);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setRole(entity.getRole() == null ? "ROLE_USER" : entity.getRole());

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public UserResponse update(Long id, UserRequest request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        String newEmail = request.getEmail().trim().toLowerCase();

        if (!user.getEmail().equals(newEmail) && repository.existsByEmail(newEmail)) {
            throw new BusinessRuleException("Ya existe otro usuario con el email: " + newEmail);
        }

        mapper.updateEntityFromRequest(request, user);
        user.setEmail(newEmail);

        // CRÍTICO: si el mapper puso password plano, lo encriptamos
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return mapper.toResponse(repository.save(user));
    }

    @Override
    public List<UserResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public UserResponse findById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return mapper.toResponse(user);
    }

    @Override
    public void delete(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        boolean hasStores = storeRepository.existsByUserId(id);
        boolean hasOrders = orderRepository.existsByUserId(id);
        boolean hasReviews = reviewRepository.existsByUserId(id);

        if (hasStores || hasOrders || hasReviews) {
            throw new BusinessRuleException("No se puede eliminar el usuario porque tiene tiendas, pedidos o reseñas asociadas");
        }

        repository.delete(user);
    }
}
