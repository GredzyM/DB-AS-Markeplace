package com.example.demo.mappers;

import com.example.demo.dtos.Order.OrderResponse;
import com.example.demo.entitites.Order;
import com.example.demo.entitites.OrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    default OrderResponse toResponse(Order entity) {
        if (entity == null) return null;

        OrderResponse dto = new OrderResponse();
        dto.setId(entity.getId());
        dto.setOrderDate(entity.getOrderDate());
        dto.setTotal(entity.getTotal());

        // ✅ Convertir enum -> String
        OrderStatus st = entity.getStatus();
        dto.setStatus(st != null ? st.name() : null);

        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
            dto.setUserEmail(entity.getUser().getEmail());
        }

        if (entity.getOrderItems() != null) {
            dto.setItems(
                    entity.getOrderItems().stream()
                            .map(item -> {
                                OrderResponse.OrderItemSummary summary = new OrderResponse.OrderItemSummary();
                                summary.setId(item.getId());
                                summary.setProductName(item.getProduct() != null ? item.getProduct().getName() : null);
                                summary.setQuantity(item.getQuantity());
                                summary.setPrice(item.getPrice());
                                summary.setSubtotal(item.getSubtotal());
                                return summary;
                            })
                            .collect(Collectors.toList())
            );
        } else {
            dto.setItems(new ArrayList<>());
        }

        return dto;
    }

    default List<OrderResponse> toResponseList(List<Order> entities) {
        if (entities == null) return new ArrayList<>();
        return entities.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
