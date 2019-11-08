package com.market.products.controllers;

import com.market.exceptions.exceptionhandlers.ApiError;
import com.market.products.api.ProductsApi;
import com.market.products.model.Product;
import com.market.products.model.ProductListResponse;
import com.market.products.model.ProductLock;
import com.market.products.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

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
    public ResponseEntity<Void> updateProduct(@Valid Product product) {
        this.productService.save(product);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @Override
    public ResponseEntity<ProductListResponse> listProductsBySeller(String idSeller) {
        List<Product> products = this.productService.findByIdSeller(idSeller);
        ProductListResponse productListResponse = new ProductListResponse();
        productListResponse.addAll(products);

        return new ResponseEntity<>(productListResponse, OK);
    }

    @Override
    public ResponseEntity<Void> deleteProduct(String idProduct) {
        this.productService.delete(idProduct);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @Override
    public ResponseEntity lockProductQuantity(@Valid ProductLock productLock) {
        if(this.productService.lockForSelling(productLock))
            return new ResponseEntity<>(OK);
        else
            return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST, "The quantity passed is larger than stock"), HttpStatus.BAD_REQUEST);
    }
}
