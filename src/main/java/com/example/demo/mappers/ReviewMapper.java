package com.example.demo.mappers;

import com.example.demo.dtos.Review.ReviewRequest;
import com.example.demo.dtos.Review.ReviewResponse;
import com.example.demo.entitites.Review;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {

    default Review toEntity(ReviewRequest dto) {
        if (dto == null) return null;
        Review entity = new Review();
        entity.setRating(dto.getRating());
        entity.setComment(dto.getComment());
        return entity;
    }

    default ReviewResponse toResponse(Review entity) {
        if (entity == null) return null;
        ReviewResponse dto = new ReviewResponse();
        dto.setId(entity.getId());
        dto.setRating(entity.getRating());
        dto.setComment(entity.getComment());
        dto.setReviewDate(entity.getReviewDate());
        
        // Mapear información del usuario y producto
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
            dto.setUserName(entity.getUser().getFirstname() + " " + entity.getUser().getLastname());
        }
        if (entity.getProduct() != null) {
            dto.setProductId(entity.getProduct().getId());
            dto.setProductName(entity.getProduct().getName());
        }
        
        return dto;
    }

    default List<ReviewResponse> toResponseList(List<Review> entities) {
        if (entities == null) return new ArrayList<>();
        return entities.stream().map(this::toResponse).collect(Collectors.toList());
    }

    default void updateEntityFromRequest(ReviewRequest dto, Review entity) {
        if (dto == null || entity == null) return;
        if (dto.getRating() != null)
            entity.setRating(dto.getRating());
        if (dto.getComment() != null)
            entity.setComment(dto.getComment());
    }
}
