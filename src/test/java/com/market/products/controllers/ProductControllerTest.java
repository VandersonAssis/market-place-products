package com.market.products.controllers;

import com.market.products.model.Product;
import com.market.products.model.ProductListResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
public class ProductControllerTest {

    @Mock
    private ProductController productsController;

    @Test
    public void shouldCallListProductsAndReturnProductListResponseObject() {
        when(this.productsController.listProducts("test_id_seller")).thenReturn(new ResponseEntity<>(this.buildProductListResponseObject(), OK));
        assertNotNull(this.productsController.listProducts("est_id_seller").getBody());
    }

    @Test
    public void shouldCheckAllRequiredFields() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Product product = new Product().idSeller("test_id").name("test_name").description("test_description").price(new BigDecimal(1));
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertTrue(violations.isEmpty());
    }

    private ProductListResponse buildProductListResponseObject() {
        ProductListResponse productListResponse = new ProductListResponse();

        for(int i = 0; i < 10; i++) {
            productListResponse.add(new Product().id("test_id" + "_" + i)
                    .idSeller("test_id_seller" + "_" + i)
                    .name("test_name" + "_" + i)
                    .description("test_description" + "_" + i)
                    .price(new BigDecimal(25.12).add(new BigDecimal(i))));
        }

        return productListResponse;
    }
}