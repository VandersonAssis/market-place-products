package com.market.products;

import com.market.products.documents.ProductDocument;
import com.market.products.documents.ProductLockDocument;
import com.market.products.model.Product;
import com.market.products.model.ProductLock;
import com.market.products.model.Seller;

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

    public static Product buildTestProduct() {
        return new Product()
                .id("test_id")
                .idSeller("test_id_seller")
                .name("test_name")
                .model("test_model")
                .description("test_description")
                .price(new BigDecimal(45))
                .quantity(10);
    }

    public static ProductLock buildTestProductLock(ProductLock.OrderStatusEnum orderStatus) {
        return new ProductLock()
                .lockId("test_product_lock_id")
                .idProduct("test_product_id")
                .quantity(2)
                .orderStatus(orderStatus);
    }

    public static Seller buildTestSeller() {
        return new Seller()
                .id("test_seller_id")
                .name("test_name");
    }
}
