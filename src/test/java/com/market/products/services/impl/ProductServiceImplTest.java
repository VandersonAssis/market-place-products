package com.market.products.services.impl;

import com.market.exceptions.custom.ResourceNotFoundException;
import com.market.products.documents.ProductDocument;
import com.market.products.documents.ProductLockDocument;
import com.market.products.model.Product;
import com.market.products.model.ProductLock;
import com.market.products.repositories.ProductLockRepository;
import com.market.products.repositories.ProductRepository;
import com.market.products.services.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ProductServiceImplTest {
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductLockRepository productLockRepository;

    @Before
    public void setUp() {
        this.productService = new ProductServiceImpl(this.productRepository, this.productLockRepository);
    }

    @Test
    public void shouldCheckIfSaveReturnProductDocument() {
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(this.buildTestProductDocument());
        ProductDocument savedDocument = this.productService.save(new Product());
        assertNotNull(savedDocument);
    }

    @Test
    public void shouldFindByIdSellerAndReturnListOfProducts() {
        when(this.productRepository.findByIdSeller(anyString())).thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class, () -> this.productService.findByIdSeller("test_id_seller"));
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

    @Test
    public void shouldLockForSellingReturnFalseWhenNoQuantityAvailable() {
        when(this.productRepository.countByIdGreaterThanEqual(anyString(), anyInt())).thenReturn(null);
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(null);

        assertTrue(this.productService.lockForSelling(new ProductLock().idProduct("test_id_product").quantity(1)).isEmpty());
    }

    @Test
    public void shouldLockForSellingReturnTrueWhenQuantityAvailable() {
        when(this.productRepository.countByIdGreaterThanEqual(anyString(), anyInt())).thenReturn(this.buildTestProductDocument());
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(null);
        when(this.productLockRepository.save(any(ProductLockDocument.class))).thenReturn(new ProductLockDocument());

        assertFalse(this.productService.lockForSelling(new ProductLock().idProduct("test_id_product").quantity(1)).isEmpty());
    }

    @Test
    public void shouldSaveProductWithChangedQuantity() {
        when(this.productRepository.countByIdGreaterThanEqual(anyString(), anyInt())).thenReturn(this.buildTestProductDocument());
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(null);
        when(this.productLockRepository.save(any(ProductLockDocument.class))).thenReturn(new ProductLockDocument());

        this.productService.lockForSelling(new ProductLock().idProduct("test_id_product").quantity(1));
        verify(this.productRepository, times(1)).save(any(ProductDocument.class));
    }

    @Test
    public void shouldSaveProductLockDocument() {
        when(this.productRepository.countByIdGreaterThanEqual(anyString(), anyInt())).thenReturn(this.buildTestProductDocument());
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(null);
        when(this.productLockRepository.save(any(ProductLockDocument.class))).thenReturn(new ProductLockDocument());

        this.productService.lockForSelling(new ProductLock().idProduct("test_id_product").quantity(1));
        verify(this.productLockRepository, times(1)).save(any(ProductLockDocument.class));
    }

    @Test
    public void shouldCallCountByIdGreaterThanEqual() {
        when(this.productRepository.countByIdGreaterThanEqual(anyString(), anyInt())).thenReturn(this.buildTestProductDocument());
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(null);
        when(this.productLockRepository.save(any(ProductLockDocument.class))).thenReturn(new ProductLockDocument());

        this.productService.lockForSelling(new ProductLock().idProduct("test_id_product").quantity(1));
        verify(this.productRepository, times(1)).countByIdGreaterThanEqual(anyString(), anyInt());
    }

    private ProductDocument buildTestProductDocument() {
        return ProductDocument.builder()
                .id("test_id")
                .idSeller("test_id_seller")
                .model("test_model")
                .name("test_name")
                .description("test_description")
                .price(new BigDecimal(10))
                .quantity(1)
                .build();
    }
}