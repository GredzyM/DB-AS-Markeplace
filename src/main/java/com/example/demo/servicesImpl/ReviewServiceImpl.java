package com.example.demo.servicesImpl;

import com.example.demo.dtos.Review.ReviewRequest;
import com.example.demo.dtos.Review.ReviewResponse;
import com.example.demo.entitites.Product;
import com.example.demo.entitites.Review;
import com.example.demo.entitites.User;
import com.example.demo.exceptions.BusinessRuleException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mappers.ReviewMapper;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.ReviewRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReviewMapper mapper;

    @Override
    public ReviewResponse create(ReviewRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + request.getUserId()));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + request.getProductId()));

        // Evitar doble reseña (además del unique constraint de BD)
        if (repository.existsByUserIdAndProductId(request.getUserId(), request.getProductId())) {
            throw new BusinessRuleException("El usuario ya dejó una reseña para este producto");
        }

        Review entity = mapper.toEntity(request);
        entity.setUser(user);
        entity.setProduct(product);
        entity.setReviewDate(new Date());

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public ReviewResponse update(Long id, ReviewRequest request) {
        Review review = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña no encontrada con ID: " + id));

        // Integridad: no permitir cambiar el autor o el producto por update
        Long currentUserId = review.getUser() != null ? review.getUser().getId() : null;
        Long currentProductId = review.getProduct() != null ? review.getProduct().getId() : null;

        if (currentUserId != null && !currentUserId.equals(request.getUserId())) {
            throw new BusinessRuleException("No se puede cambiar el usuario de una reseña");
        }
        if (currentProductId != null && !currentProductId.equals(request.getProductId())) {
            throw new BusinessRuleException("No se puede cambiar el producto de una reseña");
        }

        mapper.updateEntityFromRequest(request, review);
        return mapper.toResponse(repository.save(review));
    }

    @Override
    public List<ReviewResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public ReviewResponse findById(Long id) {
        Review review = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña no encontrada con ID: " + id));
        return mapper.toResponse(review);
    }

    @Override
    public List<ReviewResponse> findByProductId(Long productId) {
        return repository.findByProductId(productId).stream().map(mapper::toResponse).toList();
    }

    @Override
    public List<ReviewResponse> findByUserId(Long userId) {
        return repository.findByUserId(userId).stream().map(mapper::toResponse).toList();
    }

    @Override
    public void delete(Long id) {
        Review review = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reseña no encontrada con ID: " + id));
        repository.delete(review);
    }
}
