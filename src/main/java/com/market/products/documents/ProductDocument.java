package com.market.products.documents;

import com.market.products.model.Product;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "product")
public class ProductDocument {
    @Id
    private String id;
    private String idSeller;
    private String name;
    private String description;
    private BigDecimal price;

    @CreatedDate
    private LocalDateTime dateCreated;
    @LastModifiedDate
    private LocalDateTime lastModified;

    public Product convertToProduct() {
        return new Product()
                .id(this.id)
                .idSeller(this.idSeller)
                .name(this.name)
                .description(this.description)
                .price(this.price);
    }
}
