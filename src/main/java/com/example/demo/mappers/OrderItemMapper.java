package com.example.demo.mappers;

import com.example.demo.dtos.OrderItem.OrderItemRequest;
import com.example.demo.dtos.OrderItem.OrderItemResponse;
import com.example.demo.entitites.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemMapper {

    default OrderItem toEntity(OrderItemRequest dto) {
        if (dto == null) return null;
        OrderItem entity = new OrderItem();
        entity.setQuantity(dto.getQuantity());
        return entity;
    }

    default OrderItemResponse toResponse(OrderItem entity) {
        if (entity == null) return null;
        OrderItemResponse dto = new OrderItemResponse();
        dto.setId(entity.getId());
        dto.setQuantity(entity.getQuantity());
        dto.setPrice(entity.getPrice());
        dto.setSubtotal(entity.getSubtotal());
        
        // Mapear información del pedido y producto
        if (entity.getOrder() != null) {
            dto.setOrderId(entity.getOrder().getId());
        }
        if (entity.getProduct() != null) {
            dto.setProductId(entity.getProduct().getId());
            dto.setProductName(entity.getProduct().getName());
        }
        
        return dto;
    }

    default List<OrderItemResponse> toResponseList(List<OrderItem> entities) {
        if (entities == null) return new ArrayList<>();
        return entities.stream().map(this::toResponse).collect(Collectors.toList());
    }

    default void updateEntityFromRequest(OrderItemRequest dto, OrderItem entity) {
        if (dto == null || entity == null) return;
        if (dto.getQuantity() != null)
            entity.setQuantity(dto.getQuantity());
    }
}
