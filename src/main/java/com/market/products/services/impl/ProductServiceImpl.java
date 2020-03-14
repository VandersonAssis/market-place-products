package com.market.products.services.impl;

import com.market.products.documents.ProductDocument;
import com.market.products.exceptions.custom.BaseHttpException;
import com.market.products.exceptions.exceptionhandlers.ApiError;
import com.market.products.integration.sellers.services.SellersService;
import com.market.products.model.Product;
import com.market.products.repositories.ProductRepository;
import com.market.products.services.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RefreshScope
public class ProductServiceImpl implements ProductService {
    private static final Logger log = LogManager.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellersService sellersService;

    @Override
    public ProductDocument save(Product product) {
        log.info("{} begin", product.getName());
        if(this.sellersService.findById(product.getIdSeller()).isEmpty()) {
            log.error("{} seller not found!", product.getIdSeller());
            throw new BaseHttpException(new ApiError(BAD_REQUEST, "Product's seller not found"));
        }

        log.info("{} saved", product.getName());
        return this.productRepository.save(ProductDocument.build(product));
    }

    @Override
    public ProductDocument edit(Product product) {
        log.info("{} productId and {} productName begin", product.getId(), product.getName());
        if(this.productRepository.findById(product.getId()).isEmpty()) {
            log.error("{} product not found!", product.getId());
            throw new BaseHttpException(new ApiError(NOT_FOUND, "Product not found!"));
        }

        log.info("{} product edited successfully", product.getId());
        return this.productRepository.save(ProductDocument.build(product));
    }

    @Override
    public List<Product> findByIdSeller(String idSeller) {
        log.info("{} begin", idSeller);

        List<ProductDocument> sellerProducts = this.productRepository.findByIdSeller(idSeller);

        if(!sellerProducts.isEmpty()) {
            log.info("{} found", idSeller);
            return sellerProducts.stream().map(ProductDocument::convertToProduct).collect(Collectors.toList());
        }
        else {
            log.info("{} not found!", idSeller);
            throw new BaseHttpException(new ApiError(NOT_FOUND, "Seller not found"));
        }
    }

    @Override
    public void delete(String productId) {
        log.info("{} productId", productId);
        this.productRepository.deleteById(productId);
    }

    @Override
    public void deleteProducts(String sellerId) {
        log.info("All products for {} sellerId", sellerId);
        this.sellersService.findById(sellerId).ifPresentOrElse(s -> this.productRepository.deleteByIdSeller(s.getId()),
                () -> {throw new BaseHttpException(new ApiError(NOT_FOUND, "Seller not found"));});
    }

}