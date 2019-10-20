package com.market.products.documents;

import com.market.products.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "product")
@CompoundIndex(name = "idSeller_model_name", def = "{'idSeller': 1, 'model': 1, 'name': 1}", unique = true)
public class ProductDocument {
    @Id
    private String id;
    private String idSeller;
    private String model;
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
                .model(this.model)
                .name(this.name)
                .description(this.description)
                .price(this.price);
    }
}
