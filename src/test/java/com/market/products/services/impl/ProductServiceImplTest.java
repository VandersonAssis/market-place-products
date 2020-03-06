package com.market.products.services.impl;

import com.market.products.documents.ProductDocument;
import com.market.products.exceptions.custom.BaseHttpException;
import com.market.products.integration.sellers.services.SellersService;
import com.market.products.model.Seller;
import com.market.products.repositories.ProductRepository;
import com.market.products.services.ProductService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static com.market.products.TestDataBuilder.buildTestProduct;
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

    @Mock
    private SellersService sellersService;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUp() {
        this.productService = new ProductServiceImpl();
        ReflectionTestUtils.setField(this.productService, "productRepository", this.productRepository);
        ReflectionTestUtils.setField(this.productService, "sellersService", this.sellersService);
    }

    @Test
    public void shouldCheckIfSaveReturnProductDocument() {
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(buildTestProductDocument());
        when(this.sellersService.findById(anyString())).thenReturn(Optional.of(new Seller()));

        ProductDocument savedDocument = this.productService.save(buildTestProduct());
        assertNotNull(savedDocument);
    }

    @Test
    public void shouldCheckIfSaveCallsSellersFindById() {
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(buildTestProductDocument());
        when(this.sellersService.findById(anyString())).thenReturn(Optional.of(new Seller()));

        this.productService.save(buildTestProduct());
        verify(this.sellersService, times(1)).findById(anyString());
    }

    @Test
    public void shouldCheckIfSaveCallsProductRepositorySave() {
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(buildTestProductDocument());
        when(this.sellersService.findById(anyString())).thenReturn(Optional.of(new Seller()));

        this.productService.save(buildTestProduct());
        verify(this.productRepository, times(1)).save(any(ProductDocument.class));
    }

    @Test
    public void shouldThrowBaseHttpExceptionWhenNoSellerFoundOnSave() {
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(buildTestProductDocument());
        when(this.sellersService.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(BaseHttpException.class, () -> this.productService.save(buildTestProduct()));
    }

    @Test
    public void shouldEditProductSuccessfullyAndReturnProductDocument() {
        when(this.productRepository.findById(anyString())).thenReturn(Optional.of(buildTestProductDocument()));
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(buildTestProductDocument());
        ProductDocument productDocument = this.productService.edit(buildTestProduct());

        assertNotNull(productDocument);
    }

    @Test
    public void editShouldCallProductRepositoryFindById() {
        when(this.productRepository.findById(anyString())).thenReturn(Optional.of(buildTestProductDocument()));
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(buildTestProductDocument());

        this.productService.edit(buildTestProduct());

        verify(this.productRepository, times(1)).findById(anyString());
    }

    @Test
    public void editShouldCallProductRepositorySave() {
        when(this.productRepository.findById(anyString())).thenReturn(Optional.of(buildTestProductDocument()));
        when(this.productRepository.save(any(ProductDocument.class))).thenReturn(buildTestProductDocument());

        this.productService.edit(buildTestProduct());

        verify(this.productRepository, times(1)).save(any(ProductDocument.class));
    }

    @Test
    public void editShouldThrowBaseHttpExceptionWhenNoProductFound() {
        when(this.productRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(BaseHttpException.class, () -> this.productService.edit(buildTestProduct()));
    }

    @Test
    public void shouldFindByIdSellerAndReturnListOfProducts() {
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

    @Test
    public void deleteProductsBySellerShouldCallSellerFindById() {
        doNothing().when(this.productRepository).deleteByIdSeller(anyString());
        when(this.sellersService.findById(anyString())).thenReturn(Optional.of(new Seller()));

        this.productService.deleteProducts("seller_test_id");

        verify(this.sellersService, times(1)).findById(anyString());
    }

    @Test(expected = BaseHttpException.class)
    public void whenNoSellerFoundThenThrowNotFoundException() {
        doNothing().when(this.productRepository).deleteByIdSeller(anyString());
        when(this.sellersService.findById(anyString())).thenReturn(Optional.empty());

        this.productService.deleteProducts("seller_test_id");
    }
}