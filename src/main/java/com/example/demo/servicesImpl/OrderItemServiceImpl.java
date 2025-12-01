package com.example.demo.servicesImpl;

import com.example.demo.dtos.OrderItem.OrderItemRequest;
import com.example.demo.dtos.OrderItem.OrderItemResponse;
import com.example.demo.entitites.Order;
import com.example.demo.entitites.OrderItem;
import com.example.demo.entitites.Product;
import com.example.demo.exceptions.BusinessRuleException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mappers.OrderItemMapper;
import com.example.demo.repositories.OrderItemRepository;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.services.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository repository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemMapper mapper;

    @Override
    @Transactional
    public OrderItemResponse create(OrderItemRequest request) {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + request.getOrderId()));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + request.getProductId()));

        if (product.getStock() < request.getQuantity()) {
            throw new BusinessRuleException("Stock insuficiente para el producto: " + product.getName());
        }

        OrderItem entity = mapper.toEntity(request);
        entity.setOrder(order);
        entity.setProduct(product);

        // El backend decide el precio/subtotal
        entity.setPrice(product.getPrice());
        entity.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));

        // Reducir stock
        product.setStock(product.getStock() - request.getQuantity());
        productRepository.save(product);

        OrderItem saved = repository.save(entity);

        // Recalcular total del pedido
        recalcOrderTotal(order.getId());

        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public OrderItemResponse update(Long id, OrderItemRequest request) {

        OrderItem item = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item no encontrado con ID: " + id));

        Long currentOrderId = item.getOrder() != null ? item.getOrder().getId() : null;
        Long currentProductId = item.getProduct() != null ? item.getProduct().getId() : null;

        // Seguridad de integridad: no permitimos cambiar order/product desde update
        if (currentOrderId != null && !currentOrderId.equals(request.getOrderId())) {
            throw new BusinessRuleException("No se puede cambiar el pedido de un item existente");
        }
        if (currentProductId != null && !currentProductId.equals(request.getProductId())) {
            throw new BusinessRuleException("No se puede cambiar el producto de un item existente");
        }

        Product product = item.getProduct();
        int oldQty = item.getQuantity();
        int newQty = request.getQuantity();

        int diff = newQty - oldQty;

        if (diff > 0) {
            if (product.getStock() < diff) {
                throw new BusinessRuleException("Stock insuficiente para aumentar cantidad de: " + product.getName());
            }
            product.setStock(product.getStock() - diff);
            productRepository.save(product);
        } else if (diff < 0) {
            product.setStock(product.getStock() + Math.abs(diff));
            productRepository.save(product);
        }

        item.setQuantity(newQty);
        // Mantener precio original del item (precio de compra)
        item.setSubtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));

        OrderItem saved = repository.save(item);

        recalcOrderTotal(item.getOrder().getId());

        return mapper.toResponse(saved);
    }

    @Override
    public List<OrderItemResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public OrderItemResponse findById(Long id) {
        OrderItem item = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item no encontrado con ID: " + id));
        return mapper.toResponse(item);
    }

    @Override
    public List<OrderItemResponse> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId).stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {

        OrderItem item = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item no encontrado con ID: " + id));

        // Devolver stock al borrar item
        Product product = item.getProduct();
        if (product != null) {
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }

        Long orderId = item.getOrder() != null ? item.getOrder().getId() : null;

        repository.delete(item);

        if (orderId != null) {
            recalcOrderTotal(orderId);
        }
    }

    private void recalcOrderTotal(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado con ID: " + orderId));

        BigDecimal total = repository.findByOrderId(orderId).stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(total);
        orderRepository.save(order);
    }
}
