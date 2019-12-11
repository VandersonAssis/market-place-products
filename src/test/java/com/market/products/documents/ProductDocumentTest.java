package com.market.products.documents;

import com.market.products.model.Product;
import org.junit.Test;

import static com.market.products.TestDataBuilder.buildTestProductDocument;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductDocumentTest {

    @Test
    public void shouldConvertToValidProduct() {
        ProductDocument testProductDocument = buildTestProductDocument();
        Product testProduct = testProductDocument.convertToProduct();

        assertNotNull(testProduct.getId());
        assertNotNull(testProduct.getIdSeller());
        assertNotNull(testProduct.getModel());
        assertNotNull(testProduct.getName());
        assertNotNull(testProduct.getDescription());
        assertNotNull(testProduct.getPrice());
    }
}