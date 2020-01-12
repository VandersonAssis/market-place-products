package com.market.products.services.impl;
import com.market.exceptions.custom.BaseHttpException;
import com.market.exceptions.exceptionhandlers.ApiError;
import com.market.products.documents.ProductDocument;
import com.market.products.integration.sellers.services.SellersService;
import com.market.products.model.Product;
import com.market.products.repositories.ProductRepository;
import com.market.products.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RefreshScope
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellersService sellersService;

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
        if(this.sellersService.findById(product.getIdSeller()).isEmpty())
            throw new BaseHttpException(new ApiError(BAD_REQUEST, "Product's seller not found"));

        return this.productRepository.save(ProductDocument.build(product));
    }

    @Override
    public List<Product> findByIdSeller(String idSeller) {
        List<ProductDocument> sellerProducts = this.productRepository.findByIdSeller(idSeller);

        if(!sellerProducts.isEmpty())
            return sellerProducts.stream().map(ProductDocument::convertToProduct).collect(Collectors.toList());
        else
            throw new BaseHttpException(new ApiError(HttpStatus.NOT_FOUND, "Seller not found"));
    }

    @Override
    public void delete(String productId) {
        this.productRepository.deleteById(productId);
    }

}
