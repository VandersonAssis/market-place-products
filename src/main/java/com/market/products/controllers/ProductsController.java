package com.market.products.controllers;

import com.market.products.api.ProductsApi;
import com.market.products.model.Product;
import com.market.products.model.ProductListResponse;
import com.market.products.model.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.OK;

@RestController
public class ProductsController extends BaseController implements ProductsApi {

    @Override
    public ResponseEntity<ProductListResponse> listProducts() {
        return new ResponseEntity<>(this.buildTestObject(), OK);
    }

    private ProductListResponse buildTestObject() {
        ProductListResponse productListResponse = new ProductListResponse();

        for(int i = 0; i < 10; i++) {
            productListResponse.add(new ProductResponse().id("test_id" + "_" + i)
                    .idSeller("test_id_seller" + "_" + i)
                    .name("test_name" + "_" + i)
                    .description("test_description" + "_" + i)
                    .price(new BigDecimal(25.12).add(new BigDecimal(i))));
        }

        return productListResponse;
    }

    @Override
    public ResponseEntity<Void> updateProduct(@Valid Product body) {
        return null;
    }
}
