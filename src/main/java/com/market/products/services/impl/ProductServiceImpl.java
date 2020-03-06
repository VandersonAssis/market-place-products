package com.market.products.services.impl;

import com.market.products.documents.ProductDocument;
import com.market.products.exceptions.custom.BaseHttpException;
import com.market.products.exceptions.exceptionhandlers.ApiError;
import com.market.products.integration.sellers.services.SellersService;
import com.market.products.model.Product;
import com.market.products.model.Seller;
import com.market.products.repositories.ProductRepository;
import com.market.products.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RefreshScope
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellersService sellersService;

    @Override
    public ProductDocument save(Product product) {
        if(this.sellersService.findById(product.getIdSeller()).isEmpty())
            throw new BaseHttpException(new ApiError(BAD_REQUEST, "Product's seller not found"));

        return this.productRepository.save(ProductDocument.build(product));
    }

    @Override
    public ProductDocument edit(Product product) {
        if(this.productRepository.findById(product.getId()).isEmpty())
            throw new BaseHttpException(new ApiError(NOT_FOUND, "Product not found!"));

        return this.productRepository.save(ProductDocument.build(product));
    }

    @Override
    public List<Product> findByIdSeller(String idSeller) {
        List<ProductDocument> sellerProducts = this.productRepository.findByIdSeller(idSeller);

        if(!sellerProducts.isEmpty())
            return sellerProducts.stream().map(ProductDocument::convertToProduct).collect(Collectors.toList());
        else
            throw new BaseHttpException(new ApiError(NOT_FOUND, "Seller not found"));
    }

    @Override
    public void delete(String productId) {
        this.productRepository.deleteById(productId);
    }

    @Override
    public void deleteProducts(String sellerId) {
        this.sellersService.findById(sellerId).ifPresentOrElse(s -> this.productRepository.deleteByIdSeller(s.getId()),
                () -> {throw new BaseHttpException(new ApiError(NOT_FOUND, "Seller not found"));});
    }

}
