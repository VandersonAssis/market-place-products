package com.market.products.controllers;

import com.google.gson.Gson;
import com.market.products.documents.ProductDocument;
import com.market.products.model.Product;
import com.market.products.services.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Mock
    private ProductDocument productDocument;

    private MockMvc mockMvc;
    private String apiPrefix;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.productController).build();
        this.apiPrefix = "/marketplace/api/v1";
    }

    @Test
    public void shouldCallListProductsAndReturnProductListResponseObject() throws Exception {
        when(this.productService.findByIdSeller(anyString())).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get(this.apiPrefix + "/products/id_seller_test/seller")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldSaveProductReturnCreatedHttpStatusAndValidJsonBody() throws Exception {
        ProductDocument testProductDocument = this.buildTestProductDocument();
        Product testProduct = testProductDocument.convertToProduct();
        String expectedJsonResponse = new Gson().toJson(testProductDocument.convertToProduct());

        when(this.productService.save(any(Product.class))).thenReturn(testProductDocument);
        when(this.productDocument.convertToProduct()).thenReturn(testProduct);

        this.mockMvc.perform(post(this.apiPrefix + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(testProduct)))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedJsonResponse));
    }

    @Test
    public void shouldUpdateProductReturnNoContentHttpStatusAndNoBody() throws Exception {
        Product testProduct = this.buildTestProductDocument().convertToProduct();
        when(this.productService.save(any(Product.class))).thenReturn(null);

        this.mockMvc.perform(put(this.apiPrefix + "/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(testProduct)))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void shouldDeleteProductAndReturnNoContentHttpStatus() throws Exception {
        doNothing().when(this.productService).delete(anyString());
        this.mockMvc.perform(delete(this.apiPrefix + "/products/test_id_product")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void shouldCheckAllRequiredFields() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Product product = new Product().idSeller("test_id").name("test_name").description("test_description").model("test_model").price(new BigDecimal(1));
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertTrue(violations.isEmpty());
    }

    private ProductDocument buildTestProductDocument() {
        return ProductDocument.builder()
                .idSeller("test_id_seller")
                .model("test_model")
                .name("test_name")
                .description("test_description")
                .price(new BigDecimal(10))
                .build();
    }
}