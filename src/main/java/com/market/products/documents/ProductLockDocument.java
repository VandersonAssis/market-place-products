package com.market.products.documents;

import com.market.products.model.ProductLock;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "productslock")
public class ProductLockDocument {
    @Id
    private String id;
    private String customerId;
    private String productId;
    private Integer quantity;
    private OrderStatus orderStatus;

    @CreatedDate
    private LocalDateTime dateCreated;

    public static ProductLockDocument build(ProductLock productLock) {
        return new ProductLockDocument()
                .setCustomerId("to_be_implemented")
                .setProductId(productLock.getIdProduct())
                .setQuantity(productLock.getQuantity())
                .setOrderStatus(OrderStatus.valueOf(productLock.getOrderStatus().toString()));
    }

    public ProductLock convertToProductLock() {
        return new ProductLock()
            .lockId(this.id)
            .idProduct(this.getProductId())
            .quantity(this.getQuantity())
            .orderStatus(ProductLock.OrderStatusEnum.valueOf(this.orderStatus.toString()));
    }

    public enum OrderStatus {
        PENDING,
        PROCESSING,
        PAYMENT_NOT_AUTHORIZED,
        FINISHED
    }

    public String getId() {
        return id;
    }

    public ProductLockDocument setId(String id) {
        this.id = id;
        return this;
    }

    public String getCustomerId() {
        return customerId;
    }

    public ProductLockDocument setCustomerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public String getProductId() {
        return productId;
    }

    public ProductLockDocument setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductLockDocument setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public ProductLockDocument setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public ProductLockDocument setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }
}
