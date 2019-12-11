package com.market.products;

import com.market.products.documents.ProductDocument;
import com.market.products.documents.ProductLockDocument;

import java.math.BigDecimal;

public abstract class TestDataBuilder {
    public static ProductDocument buildTestProductDocument() {
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

    public static ProductLockDocument buildTestProductLockDocument() {
        return ProductLockDocument.builder()
                .id("test_id")
                .customerId("test_customer_id")
                .productId("test_product_id")
                .quantity(1)
                .orderStatus(ProductLockDocument.OrderStatus.PENDING)
                .build();
    }
}
