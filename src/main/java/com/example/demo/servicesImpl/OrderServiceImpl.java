package com.example.demo.servicesImpl;

import com.example.demo.dtos.Order.OrderRequest;
import com.example.demo.dtos.Order.OrderResponse;
import com.example.demo.entitites.Order;
import com.example.demo.entitites.OrderItem;
import com.example.demo.entitites.OrderStatus;
import com.example.demo.entitites.Product;
import com.example.demo.entitites.User;
import com.example.demo.exceptions.BusinessRuleException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mappers.OrderMapper;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper mapper;

    @Override
    @Transactional
    public OrderResponse create(OrderRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con ID: " + request.getUserId()
                ));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderRequest.OrderItemData itemData : request.getItems()) {

            Product product = productRepository.findById(itemData.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Producto no encontrado con ID: " + itemData.getProductId()
                    ));

            if (product.getStore() != null && !product.getStore().isActive()) {
                throw new BusinessRuleException(
                        "No se puede comprar productos de una tienda inactiva: " + product.getStore().getName()
                );
            }

            if (product.getStock() < itemData.getQuantity()) {
                throw new BusinessRuleException("Stock insuficiente para el producto: " + product.getName());
            }

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setOrder(order);
            item.setQuantity(itemData.getQuantity());
            item.setPrice(product.getPrice());
            item.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(itemData.getQuantity())));

            items.add(item);
            total = total.add(item.getSubtotal());

            product.setStock(product.getStock() - itemData.getQuantity());
            productRepository.save(product);
        }

        order.setOrderItems(items);
        order.setTotal(total);

        return mapper.toResponse(repository.save(order));
    }

    @Override
    @Transactional
    public OrderResponse updateStatus(Long id, String status) {

        Order order = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + id));

        if (status == null || status.isBlank()) {
            throw new BusinessRuleException("El estado es obligatorio");
        }

        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BusinessRuleException("Estado inválido. Valores permitidos: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED");
        }

        // Regla: si ya está finalizado, no permitir cambios
        if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessRuleException("No se puede cambiar el estado de un pedido ya finalizado");
        }

        order.setStatus(newStatus);
        return mapper.toResponse(repository.save(order));
    }

    @Override
    public List<OrderResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public OrderResponse findById(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + id));
        return mapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> findByUserId(Long userId) {
        return repository.findByUserId(userId).stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {

        Order order = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + id));

        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new BusinessRuleException("No se puede eliminar un pedido ya enviado o entregado");
        }

        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                Product product = item.getProduct();
                if (product != null) {
                    product.setStock(product.getStock() + item.getQuantity());
                    productRepository.save(product);
                }
            }
        }

        repository.delete(order);
    }
}
