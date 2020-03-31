package com.market.products.services.impl;

import com.market.products.documents.ProductDocument;
import com.market.products.documents.ProductLockDocument;
import com.market.products.model.ProductLock;
import com.market.products.repositories.ProductLockRepository;
import com.market.products.repositories.ProductRepository;
import com.market.products.services.ProductLockService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static com.market.products.TestDataBuilder.buildTestProductDocument;
import static com.market.products.TestDataBuilder.buildTestProductLockDocument;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ProductLockServiceImplTest {
    private ProductLockService productLockService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductLockRepository productLockRepository;

    @Before
    public void setUp() {
        this.productLockService = new ProductLockServiceImpl();
        ReflectionTestUtils.setField(this.productLockService, "productRepository", this.productRepository);
        ReflectionTestUtils.setField(this.productLockService, "productLockRepository", this.productLockRepository);
    }

    @Test
    public void shouldLockForSellingReturnFalseWhenNoQuantityAvailable() {
        when(this.productRepository.countByIdGreaterThanEqual(anyString(), anyInt())).thenReturn(null);
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(null);

        assertTrue(this.productLockService.lockForSelling(new ProductLock().idProduct("test_id_product").quantity(1)).isEmpty());
    }

    @Test
    public void shouldLockForSellingReturnTrueWhenQuantityAvailable() {
        when(this.productRepository.countByIdGreaterThanEqual(anyString(), anyInt())).thenReturn(buildTestProductDocument());
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(null);
        when(this.productLockRepository.save(any(ProductLockDocument.class))).thenReturn(buildTestProductLockDocument());

        assertFalse(this.productLockService.lockForSelling(new ProductLock().idProduct("test_id_product").quantity(1)).isEmpty());
    }

    @Test
    public void shouldSaveProductWithChangedQuantity() {
        when(this.productRepository.countByIdGreaterThanEqual(anyString(), anyInt())).thenReturn(buildTestProductDocument());
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(null);
        when(this.productLockRepository.save(any(ProductLockDocument.class))).thenReturn(buildTestProductLockDocument());

        this.productLockService.lockForSelling(new ProductLock().idProduct("test_id_product").quantity(1));
        verify(this.productRepository, times(1)).save(any(ProductDocument.class));
    }

    @Test
    public void shouldSaveProductLockDocument() {
        when(this.productRepository.countByIdGreaterThanEqual(anyString(), anyInt())).thenReturn(buildTestProductDocument());
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(null);
        when(this.productLockRepository.save(any(ProductLockDocument.class))).thenReturn(buildTestProductLockDocument());

        this.productLockService.lockForSelling(new ProductLock().idProduct("test_id_product").quantity(1));
        verify(this.productLockRepository, times(1)).save(any(ProductLockDocument.class));
    }

    @Test
    public void shouldCallCountByIdGreaterThanEqual() {
        when(this.productRepository.countByIdGreaterThanEqual(anyString(), anyInt())).thenReturn(buildTestProductDocument());
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(null);
        when(this.productLockRepository.save(any(ProductLockDocument.class))).thenReturn(buildTestProductLockDocument());

        this.productLockService.lockForSelling(new ProductLock().idProduct("test_id_product").quantity(1));
        verify(this.productRepository, times(1)).countByIdGreaterThanEqual(anyString(), anyInt());
    }

    @Test
    public void shouldReturnOptionalProductLockWhenFindingByIdAndCallingProductLockRepository() {
        when(this.productLockRepository.findById(anyString())).thenReturn(Optional.of(buildTestProductLockDocument()));

        Optional<ProductLock> productLockFound = this.productLockService.findById("test_id");
        assertFalse(productLockFound.isEmpty());
        verify(this.productLockRepository, times(1)).findById(anyString());
    }

    @Test
    public void shouldCheckIfProductLockRepositoryFindByIdIsCalled() {
        ProductDocument testProductDocument = buildTestProductDocument();

        when(this.productLockRepository.findById(anyString())).thenReturn(Optional.of(buildTestProductLockDocument()));
        when(this.productRepository.findById(anyString())).thenReturn(Optional.of(testProductDocument));
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(testProductDocument);
        doNothing().when(this.productLockRepository).delete(any(ProductLockDocument.class));

        this.productLockService.unlockForSelling("test_id");
        verify(this.productLockRepository, times(1)).findById(anyString());
    }

    @Test
    public void shouldCheckIfProductRepositoryFindByIdIsCalled() {
        ProductDocument testProductDocument = buildTestProductDocument();

        when(this.productLockRepository.findById(anyString())).thenReturn(Optional.of(buildTestProductLockDocument()));
        when(this.productRepository.findById(anyString())).thenReturn(Optional.of(testProductDocument));
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(testProductDocument);
        doNothing().when(this.productLockRepository).delete(any(ProductLockDocument.class));

        this.productLockService.unlockForSelling("test_id");

        verify(this.productRepository, times(1)).findById(anyString());
    }

    @Test
    public void shouldCheckIfProductIsSavedWithNewQuantity() {
        ProductDocument testProductDocument = buildTestProductDocument();

        when(this.productLockRepository.findById(anyString())).thenReturn(Optional.of(buildTestProductLockDocument()));
        when(this.productRepository.findById(anyString())).thenReturn(Optional.of(testProductDocument));
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(testProductDocument);
        doNothing().when(this.productLockRepository).delete(any(ProductLockDocument.class));

        this.productLockService.unlockForSelling("test_id");

        verify(this.productRepository, times(1)).save(any(ProductDocument.class));
    }

    @Test
    public void shouldCheckIfTheLockedProductIsDeletedAfterUnlockingFluxEnds() {
        ProductDocument testProductDocument = buildTestProductDocument();

        when(this.productLockRepository.findById(anyString())).thenReturn(Optional.of(buildTestProductLockDocument()));
        when(this.productRepository.findById(anyString())).thenReturn(Optional.of(testProductDocument));
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(testProductDocument);
        doNothing().when(this.productLockRepository).delete(any(ProductLockDocument.class));

        this.productLockService.unlockForSelling("test_id");

        verify(this.productLockRepository, times(1)).delete(any(ProductLockDocument.class));
    }

    @Test
    public void shouldReturnTrueAfterSuccessfulUnlocking() {
        ProductDocument testProductDocument = buildTestProductDocument();

        when(this.productLockRepository.findById(anyString())).thenReturn(Optional.of(buildTestProductLockDocument()));
        when(this.productRepository.findById(anyString())).thenReturn(Optional.of(testProductDocument));
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(testProductDocument);
        doNothing().when(this.productLockRepository).delete(any(ProductLockDocument.class));

        assertTrue(this.productLockService.unlockForSelling("test_id"));
    }

    @Test
    public void shouldReturnFalseAfterUnsuccessfulUnlocking() {
        ProductDocument testProductDocument = buildTestProductDocument();

        when(this.productLockRepository.findById(anyString())).thenReturn(Optional.empty());
        when(this.productRepository.findById(anyString())).thenReturn(Optional.of(testProductDocument));
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(testProductDocument);
        doNothing().when(this.productLockRepository).delete(any(ProductLockDocument.class));

        assertFalse(this.productLockService.unlockForSelling("test_id"));
    }

    @Test
    public void shouldCheckIfDeleteByIdCallsProductLockRepositoryDeleteById() {
        doNothing().when(this.productLockRepository).deleteById(anyString());

        this.productLockService.deleteById("test_id");
        verify(this.productLockRepository, times(1)).deleteById(anyString());
    }
}