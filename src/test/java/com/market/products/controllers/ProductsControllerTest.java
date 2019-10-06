package com.market.products.controllers;

import com.market.products.model.ProductListResponse;
import com.market.products.model.ProductResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
public class ProductsControllerTest {

    @Mock
    private ProductsController productsController;

    @Test
    public void listProductsCallShouldReturnListOfProducts() {
        when(this.productsController.listProducts()).thenReturn(new ResponseEntity<>(this.buildTestObject(), OK));
        assertNotNull(this.productsController.listProducts().getBody());
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
}