package com.market.products.controllers;

import com.google.gson.Gson;
import com.market.products.documents.ProductDocument;
import com.market.products.exceptions.exceptionhandlers.ExceptionHandlers;
import com.market.products.model.Product;
import com.market.products.model.ProductLock;
import com.market.products.services.ProductLockService;
import com.market.products.services.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import static com.market.products.TestDataBuilder.buildTestProductDocument;
import static com.market.products.TestDataBuilder.buildTestProductLockDocument;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Mock
    private ProductLockService productLockService;

    @Mock
    private ProductDocument productDocument;

    private MockMvc mockMvc;
    private String apiPrefix;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.productController)
                .setControllerAdvice(new ExceptionHandlers())
                .build();
        this.apiPrefix = "/marketplace/api/v1";
    }

    @Test
    public void shouldCallListProductsAndReturnProductListResponseObject() throws Exception {
        when(this.productService.findByIdSeller(anyString())).thenReturn(new ArrayList<>());
        this.mockMvc.perform(get(this.apiPrefix + "/products/id_seller_test/seller")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldSaveProductReturnCreatedHttpStatusAndValidJsonBody() throws Exception {
        ProductDocument testProductDocument = buildTestProductDocument();
        Product testProduct = testProductDocument.convertToProduct();
        String expectedJsonResponse = new Gson().toJson(testProductDocument.convertToProduct());

        when(this.productService.save(any(Product.class))).thenReturn(testProductDocument);
        when(this.productDocument.convertToProduct()).thenReturn(testProduct);

        this.mockMvc.perform(post(this.apiPrefix + "/products")
                .contentType(APPLICATION_JSON)
                .content(new Gson().toJson(testProduct)))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedJsonResponse));
    }

    @Test
    public void shouldUpdateProductReturnNoContentHttpStatusAndNoBody() throws Exception {
        Product testProduct = buildTestProductDocument().convertToProduct();
        when(this.productService.save(any(Product.class))).thenReturn(null);

        this.mockMvc.perform(put(this.apiPrefix + "/products")
                .contentType(APPLICATION_JSON)
                .content(new Gson().toJson(testProduct)))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void shouldCallProductControllerLockForSellingMethodAndReturnHttpOk() throws Exception {
        when(this.productLockService.lockForSelling(any(ProductLock.class))).thenReturn(Optional.of(new ProductLock()));

        this.mockMvc.perform(post(this.apiPrefix + "/products/lock")
                .contentType(APPLICATION_JSON)
                .content(new Gson().toJson(new ProductLock().idProduct("test_id_product").quantity(1))))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCallProductControllerLockForSellingMethodAndReturnHttpBadRequest() throws Exception {
        when(this.productLockService.lockForSelling(any(ProductLock.class))).thenReturn(Optional.empty());

        this.mockMvc.perform(post(this.apiPrefix + "/products/lock")
                .contentType(APPLICATION_JSON)
                .content(new Gson().toJson(new ProductLock().idProduct("test_id_product").quantity(1))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldCheckAllProductLockRequiredFields() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        ProductLock productLockToggle = new ProductLock().idProduct("test_id_product").quantity(1);
        Set<ConstraintViolation<ProductLock>> violations = validator.validate(productLockToggle);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void shouldDeleteProductAndReturnNoContentHttpStatus() throws Exception {
        doNothing().when(this.productService).delete(anyString());
        this.mockMvc.perform(delete(this.apiPrefix + "/products/test_id_product")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void shouldCallGetProductLockAndReturnHttpOkWithContent() throws Exception {
        when(this.productLockService.findById(anyString()))
                .thenReturn(Optional.of(buildTestProductLockDocument().convertToProductLock()));

        this.mockMvc.perform(get(this.apiPrefix + "/products/test_id_product_lock/lock")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void shouldCallGetProductLockAndReturnHttpNotFound() throws Exception {
        when(this.productLockService.findById(anyString()))
                .thenReturn(Optional.empty());

        this.mockMvc.perform(get(this.apiPrefix + "/products/test_id_product_lock/lock")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCallUnlockProductQuantityAndReturnOkHttpStatus() throws Exception {
        when(this.productLockService.unlockForSelling(anyString())).thenReturn(true);

        this.mockMvc.perform(post(this.apiPrefix + "/products/unlock")
                .contentType(TEXT_PLAIN)
                .content("test_id_product_lock"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCallUnlockProductQuantityAndReturnNotFoundHttpStatus() throws Exception {
        when(this.productLockService.unlockForSelling(anyString())).thenReturn(false);

        this.mockMvc.perform(post(this.apiPrefix + "/products/unlock")
                .contentType(TEXT_PLAIN)
                .content("test_id_product_lock"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCallDeleteLockAndReturnOk() throws Exception {
        doNothing().when(this.productLockService).deleteById(anyString());

        this.mockMvc.perform(delete(this.apiPrefix + "/products/test_id_product_lock/lock")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldCheckAllProductRequiredFields() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Product product = new Product().idSeller("test_id").name("test_name").description("test_description")
                .model("test_model").price(new BigDecimal(1))
                .quantity(1);
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertTrue(violations.isEmpty());
    }
}