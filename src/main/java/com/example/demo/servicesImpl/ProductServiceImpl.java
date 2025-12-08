package com.example.demo.servicesImpl;

import com.example.demo.dtos.Product.ProductRequest;
import com.example.demo.dtos.Product.ProductResponse;
import com.example.demo.entitites.Product;
import com.example.demo.entitites.Store;
import com.example.demo.exceptions.BusinessRuleException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mappers.ProductMapper;
import com.example.demo.repositories.OrderItemRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.StoreRepository;
import com.example.demo.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final StoreRepository storeRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductMapper mapper;

    @Override
    public ProductResponse create(ProductRequest request) {
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Tienda no encontrada con ID: " + request.getStoreId()));

        if (!store.isActive()) {
            throw new BusinessRuleException("No se puede agregar productos a una tienda inactiva");
        }

        Product entity = mapper.toEntity(request);
        entity.setStore(store);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        // Integridad: no permitir mover el producto a otra tienda por update
        Long currentStoreId = product.getStore() != null ? product.getStore().getId() : null;
        if (currentStoreId != null && !currentStoreId.equals(request.getStoreId())) {
            throw new BusinessRuleException("No se puede cambiar la tienda de un producto existente");
        }

        mapper.updateEntityFromRequest(request, product);

        // Asegurar que no se pierde la referencia a store por el mapper
        if (product.getStore() == null && currentStoreId != null) {
            Store store = storeRepository.findById(currentStoreId)
                    .orElseThrow(() -> new ResourceNotFoundException("Tienda no encontrada con ID: " + currentStoreId));
            product.setStore(store);
        }

        return mapper.toResponse(repository.save(product));
    }

    @Override
    public List<ProductResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public ProductResponse findById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        return mapper.toResponse(product);
    }

    @Override
    public List<ProductResponse> findByStoreId(Long storeId) {
        return repository.findByStoreId(storeId).stream().map(mapper::toResponse).toList();
    }

    @Override
    public void delete(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        // Mejor que tocar LAZY collections
        if (orderItemRepository.existsByProductId(id)) {
            throw new BusinessRuleException("No se puede eliminar el producto porque está en pedidos existentes");
        }

        repository.delete(product);
    }
}
