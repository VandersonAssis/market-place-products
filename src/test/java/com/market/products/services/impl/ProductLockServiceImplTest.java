//TODO Finish refactoring these tests

//package com.market.products.services.impl;
//
//import com.market.products.documents.ProductDocument;
//import com.market.products.documents.ProductLockDocument;
//import com.market.products.model.ProductLock;
//import com.market.products.repositories.ProductRepository;
//import com.market.products.services.ProductLockService;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@RunWith(SpringRunner.class)
//public class ProductLockServiceImplTest {
//    private ProductLockService productLockService;
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @Before
//    public void setUp() {
//        this.productLockService = new ProductLockServiceImpl(this.product);
//    }
//
//    @Test
//    public void shouldLockForSellingReturnFalseWhenNoQuantityAvailable() {
//        when(this.productRepository.countByIdGreaterThanEqual(anyString(), anyInt())).thenReturn(null);
//        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(null);
//
//        assertTrue(this.productLockService.lockForSelling(new ProductLock().idProduct("test_id_product").quantity(1)).isEmpty());
//    }
//
//    @Test
//    public void shouldLockForSellingReturnTrueWhenQuantityAvailable() {
//        when(this.productRepository.countByIdGreaterThanEqual(anyString(), anyInt())).thenReturn(this.buildTestProductDocument());
//        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(null);
//        when(this.productLockRepository.save(any(ProductLockDocument.class))).thenReturn(new ProductLockDocument());
//
//        assertFalse(this.productLockService.lockForSelling(new ProductLock().idProduct("test_id_product").quantity(1)).isEmpty());
//    }
//
//    @Test
//    public void shouldSaveProductWithChangedQuantity() {
//        when(this.productRepository.countByIdGreaterThanEqual(anyString(), anyInt())).thenReturn(this.buildTestProductDocument());
//        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(null);
//        when(this.productLockRepository.save(any(ProductLockDocument.class))).thenReturn(new ProductLockDocument());
//
//        this.productLockService.lockForSelling(new ProductLock().idProduct("test_id_product").quantity(1));
//        verify(this.productRepository, times(1)).save(any(ProductDocument.class));
//    }
//
//    @Test
//    public void shouldSaveProductLockDocument() {
//        when(this.productRepository.countByIdGreaterThanEqual(anyString(), anyInt())).thenReturn(this.buildTestProductDocument());
//        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(null);
//        when(this.productLockRepository.save(any(ProductLockDocument.class))).thenReturn(new ProductLockDocument());
//
//        this.productLockService.lockForSelling(new ProductLock().idProduct("test_id_product").quantity(1));
//        verify(this.productLockRepository, times(1)).save(any(ProductLockDocument.class));
//    }
//
//    @Test
//    public void shouldCallCountByIdGreaterThanEqual() {
//        when(this.productRepository.countByIdGreaterThanEqual(anyString(), anyInt())).thenReturn(this.buildTestProductDocument());
//        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(null);
//        when(this.productLockRepository.save(any(ProductLockDocument.class))).thenReturn(new ProductLockDocument());
//
//        this.productLockService.lockForSelling(new ProductLock().idProduct("test_id_product").quantity(1));
//        verify(this.productRepository, times(1)).countByIdGreaterThanEqual(anyString(), anyInt());
//    }
//}