package com.market.products.controllers;

import com.market.products.api.ProductsApi;
import com.market.products.model.Product;
import com.market.products.model.ProductListResponse;
import com.market.products.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.*;

@RestController
public class ProductController extends BaseController implements ProductsApi {

    @Autowired
    private ProductService productService;

    @Override
    public ResponseEntity<Product> saveProduct(@Valid Product product) {
        Product savedProduct = this.productService.save(product).convertToProduct();
        return new ResponseEntity<>(savedProduct, CREATED);
    }

    @Override
    public ResponseEntity<ProductListResponse> listProducts() {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateProduct(@Valid Product body) {
        return null;
    }
}
