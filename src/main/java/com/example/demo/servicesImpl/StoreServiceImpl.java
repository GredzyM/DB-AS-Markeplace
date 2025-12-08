package com.example.demo.servicesImpl;

import com.example.demo.dtos.Store.StoreRequest;
import com.example.demo.dtos.Store.StoreResponse;
import com.example.demo.entitites.Store;
import com.example.demo.entitites.User;
import com.example.demo.exceptions.BusinessRuleException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mappers.StoreMapper;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.StoreRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository repository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final StoreMapper mapper;

    @Override
    public StoreResponse create(StoreRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + request.getUserId()));

        Store entity = mapper.toEntity(request);
        entity.setUser(user);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public StoreResponse update(Long id, StoreRequest request) {
        Store store = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tienda no encontrada con ID: " + id));

        Long currentUserId = store.getUser() != null ? store.getUser().getId() : null;
        if (currentUserId != null && !currentUserId.equals(request.getUserId())) {
            throw new BusinessRuleException("No se puede cambiar el propietario de una tienda existente");
        }

        mapper.updateEntityFromRequest(request, store);
        return mapper.toResponse(repository.save(store));
    }

    @Override
    public List<StoreResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public StoreResponse findById(Long id) {
        Store store = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tienda no encontrada con ID: " + id));
        return mapper.toResponse(store);
    }

    @Override
    public List<StoreResponse> findByUserId(Long userId) {
        return repository.findByUserId(userId).stream().map(mapper::toResponse).toList();
    }

    @Override
    public void delete(Long id) {
        Store store = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tienda no encontrada con ID: " + id));

        // Evitar depender de colecciones LAZY
        boolean hasProducts = productRepository.existsByStoreId(id);

        if (hasProducts) {
            // Soft delete: marcar como inactiva
            store.setActive(false);
            repository.save(store);
        } else {
            repository.delete(store);
        }
    }
}
