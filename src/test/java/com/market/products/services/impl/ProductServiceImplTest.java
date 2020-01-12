package com.market.products.services.impl;

import com.market.exceptions.custom.BaseHttpException;
import com.market.products.documents.ProductDocument;
import com.market.products.model.Product;
import com.market.products.repositories.ProductRepository;
import com.market.products.services.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static com.market.products.TestDataBuilder.buildTestProductDocument;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ProductServiceImplTest {
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Before
    public void setUp() {
        this.productService = new ProductServiceImpl(this.productRepository);
    }

    @Test
    public void shouldCheckIfSaveReturnProductDocument() {
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(buildTestProductDocument());
        ProductDocument savedDocument = this.productService.save(new Product());
        assertNotNull(savedDocument);
    }

    @Test
    public void shouldFindByIdSellerAndReturnListOfProducts() {
        //TODO Find a way to check which http error is wrapped inside bellow baseHttpException
        when(this.productRepository.findByIdSeller(anyString())).thenReturn(new ArrayList<>());
        assertThrows(BaseHttpException.class, () -> this.productService.findByIdSeller("test_id_seller"));
    }

    @Test
    public void deleteShouldCallRepositoryDeleteMethod() {
        doNothing().when(this.productRepository).deleteById(anyString());
        this.productService.delete("test_product_id");
        verify(this.productRepository, times(1)).deleteById(anyString());
    }

    @Test
    public void shouldCheckIfFinByIdSellerReturnsProductsList() {
        when(this.productRepository.findByIdSeller(anyString())).thenReturn(Arrays.asList(new ProductDocument(), new ProductDocument()));
        assertNotNull(this.productService.findByIdSeller("test_id_seller"));
    }
}