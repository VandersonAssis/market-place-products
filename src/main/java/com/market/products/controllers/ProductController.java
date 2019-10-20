package com.market.products.controllers;

import com.market.products.api.ProductsApi;
import com.market.products.model.Product;
import com.market.products.model.ProductListResponse;
import com.market.products.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<ProductListResponse> listProductsBySeller(String idSeller) {
        List<Product> products = this.productService.findByIdSeller(idSeller);
        ProductListResponse productListResponse = new ProductListResponse();
        productListResponse.addAll(products);

        return new ResponseEntity<>(productListResponse, OK);
    }

    @Override
    public ResponseEntity<Void> delete(String idProduct) {
        this.productService.delete(idProduct);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> updateProduct(@Valid Product body) {
        return null;
    }
}
