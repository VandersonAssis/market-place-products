package com.market.products.documents;

import com.market.products.model.ProductLock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
        return ProductLockDocument.builder()
                .customerId("to_be_implemented")
                .productId(productLock.getIdProduct())
                .quantity(productLock.getQuantity())
                .orderStatus(OrderStatus.valueOf(productLock.getOrderStatus().toString()))
                .build();
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
}
