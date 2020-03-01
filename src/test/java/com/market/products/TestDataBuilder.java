package com.market.products;

import com.market.products.documents.ProductDocument;
import com.market.products.documents.ProductLockDocument;

import java.math.BigDecimal;

public abstract class TestDataBuilder {
    public static ProductDocument buildTestProductDocument() {
        return new ProductDocument()
                .setId("test_id")
                .setIdSeller("test_id_seller")
                .setModel("test_model")
                .setName("test_name")
                .setDescription("test_description")
                .setPrice(new BigDecimal(10))
                .setQuantity(1);
    }

    public static ProductLockDocument buildTestProductLockDocument() {
        return new ProductLockDocument()
                .setId("test_id")
                .setCustomerId("test_customer_id")
                .setProductId("test_product_id")
                .setQuantity(1)
                .setOrderStatus(ProductLockDocument.OrderStatus.PENDING);
    }
}
