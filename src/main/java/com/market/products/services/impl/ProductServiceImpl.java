package com.market.products.services.impl;

import com.market.exceptions.custom.ResourceNotFoundException;
import com.market.products.documents.ProductDocument;
import com.market.products.model.Product;
import com.market.products.repositories.ProductRepository;
import com.market.products.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Value("${current.version}")
    private String currentVersion;

    ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public String findSystemCurrentVersion() {
        return this.currentVersion;
    }

    @Override
    public ProductDocument save(Product product) {
        return this.productRepository.save(ProductDocument.build(product));
    }

    @Override
    public List<Product> findByIdSeller(String idSeller) {
        List<ProductDocument> sellerProducts = this.productRepository.findByIdSeller(idSeller);

        if(!sellerProducts.isEmpty())
            return sellerProducts.stream().map(ProductDocument::convertToProduct).collect(Collectors.toList());
        else
            throw new ResourceNotFoundException();
    }

    @Override
    public void delete(String productId) {
        this.productRepository.deleteById(productId);
    }

}
