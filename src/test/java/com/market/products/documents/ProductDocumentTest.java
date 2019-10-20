package com.market.products.documents;

import com.market.products.model.Product;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductDocumentTest {

    @Test
    public void shouldConvertToValidProduct() {
        ProductDocument testProductDocument = this.buildTestProductDocument();
        Product testProduct = testProductDocument.convertToProduct();

        assertNotNull(testProduct.getId());
        assertNotNull(testProduct.getIdSeller());
        assertNotNull(testProduct.getModel());
        assertNotNull(testProduct.getName());
        assertNotNull(testProduct.getDescription());
        assertNotNull(testProduct.getPrice());
    }

    private ProductDocument buildTestProductDocument() {
        return ProductDocument.builder()
                .id("test_id")
                .idSeller("test_id_seller")
                .model("test_model")
                .name("test_name")
                .description("test_description")
                .price(new BigDecimal(10))
                .build();
    }
}