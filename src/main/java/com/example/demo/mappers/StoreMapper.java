
package com.example.demo.mappers;

import com.example.demo.dtos.Store.StoreRequest;
import com.example.demo.dtos.Store.StoreResponse;
import com.example.demo.entitites.Store;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StoreMapper {

    default Store toEntity(StoreRequest dto) {
        if (dto == null) return null;
        Store entity = new Store();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    default StoreResponse toResponse(Store entity) {
        if (entity == null) return null;
        StoreResponse dto = new StoreResponse();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setActive(entity.isActive());
        
        // Mapear información del propietario
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
            dto.setUserEmail(entity.getUser().getEmail());
        }
        
        return dto;
    }

    default List<StoreResponse> toResponseList(List<Store> entities) {
        if (entities == null) return new ArrayList<>();
        return entities.stream().map(this::toResponse).collect(Collectors.toList());
    }

    default void updateEntityFromRequest(StoreRequest dto, Store entity) {
        if (dto == null || entity == null) return;
        if (dto.getName() != null && !dto.getName().isBlank())
            entity.setName(dto.getName());
        if (dto.getDescription() != null)
            entity.setDescription(dto.getDescription());
    }
}

