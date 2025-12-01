package com.example.demo.mappers;

import com.example.demo.dtos.User.UserRequest;
import com.example.demo.dtos.User.UserResponse;
import com.example.demo.entitites.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    default User toEntity(UserRequest dto) {
        if (dto == null) return null;
        User entity = new User();
        entity.setFirstname(dto.getFirstname());
        entity.setLastname(dto.getLastname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword()); // Se encriptará en el servicio
        return entity;
    }

    default UserResponse toResponse(User entity) {
        if (entity == null) return null;
        UserResponse dto = new UserResponse();
        dto.setId(entity.getId());
        dto.setFirstname(entity.getFirstname());
        dto.setLastname(entity.getLastname());
        dto.setEmail(entity.getEmail());
        dto.setRole(entity.getRole());
        // NO incluimos password por seguridad
        return dto;
    }

    default List<UserResponse> toResponseList(List<User> entities) {
        if (entities == null) return new ArrayList<>();
        return entities.stream().map(this::toResponse).collect(Collectors.toList());
    }

    default void updateEntityFromRequest(UserRequest dto, User entity) {
        if (dto == null || entity == null) return;
        if (dto.getFirstname() != null && !dto.getFirstname().isBlank())
            entity.setFirstname(dto.getFirstname());
        if (dto.getLastname() != null && !dto.getLastname().isBlank())
            entity.setLastname(dto.getLastname());
        //if (dto.getEmail() != null && !dto.getEmail().isBlank())
         //   entity.setEmail(dto.getEmail());
        // ❌ NO tocar email aquí → lo controla el servicio (trim/lower + unique)

        // ❌ NO tocar password aquí → lo controla el servicio (encode)
    }
}
