package com.example.demo.mappers;

import com.example.demo.dtos.Product.ProductRequest;
import com.example.demo.dtos.Product.ProductResponse;
import com.example.demo.entitites.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    default Product toEntity(ProductRequest dto) {
        if (dto == null) return null;
        Product entity = new Product();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setStock(dto.getStock());
        return entity;
    }

    default ProductResponse toResponse(Product entity) {
        if (entity == null) return null;
        ProductResponse dto = new ProductResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setStock(entity.getStock());
        
        // Mapear información de la tienda
        if (entity.getStore() != null) {
            dto.setStoreId(entity.getStore().getId());
            dto.setStoreName(entity.getStore().getName());
        }
        
        return dto;
    }

    default List<ProductResponse> toResponseList(List<Product> entities) {
        if (entities == null) return new ArrayList<>();
        return entities.stream().map(this::toResponse).collect(Collectors.toList());
    }

    default void updateEntityFromRequest(ProductRequest dto, Product entity) {
        if (dto == null || entity == null) return;
        if (dto.getName() != null && !dto.getName().isBlank())
            entity.setName(dto.getName());
        if (dto.getDescription() != null)
            entity.setDescription(dto.getDescription());
        if (dto.getPrice() != null)
            entity.setPrice(dto.getPrice());
        if (dto.getStock() != null)
            entity.setStock(dto.getStock());
    }
}
