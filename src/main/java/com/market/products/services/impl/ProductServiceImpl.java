package com.market.products.services.impl;

import com.market.products.documents.ProductDocument;
import com.market.products.model.Product;
import com.market.products.repositories.ProductRepository;
import com.market.products.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
    public boolean update(Product product) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }
}
