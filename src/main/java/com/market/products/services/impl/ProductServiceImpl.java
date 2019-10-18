package com.market.products.services.impl;

import com.market.exceptions.custom.ResourceNotFoundException;
import com.market.products.documents.ProductDocument;
import com.market.products.model.Product;
import com.market.products.repositories.ProductRepository;
import com.market.products.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductDocument save(Product product) {
        return this.productRepository.save(ProductDocument.builder()
                .idSeller(product.getIdSeller())
                .model(product.getModel())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .dateCreated(LocalDateTime.now())
                .lastModified(LocalDateTime.now())
                .build());
    }

    @Override
    public List<Product> findByIdSeller(String idSeller) {
        List<Product> products = this.productRepository.findByIdSeller(idSeller).stream().map(ProductDocument::convertToProduct).collect(Collectors.toList());
        if(products.isEmpty()) throw new ResourceNotFoundException();

        return products;
    }

    @Override
    public boolean update(Product product) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }
}
