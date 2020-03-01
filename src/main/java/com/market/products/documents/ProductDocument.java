package com.market.products.documents;

import com.market.products.model.Product;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "products")
@CompoundIndex(name = "idSeller_model_name", def = "{'idSeller': 1, 'model': 1, 'name': 1}", unique = true)
public class ProductDocument {
    @Id
    private String id;
    private String idSeller;
    private String model;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;

    @CreatedDate
    private LocalDateTime dateCreated;
    @LastModifiedDate
    private LocalDateTime lastModified;



    public static ProductDocument build(Product product) {
        return new ProductDocument()
                .setId(product.getId())
                .setIdSeller(product.getIdSeller())
                .setModel(product.getModel())
                .setName(product.getName())
                .setDescription(product.getDescription())
                .setPrice(product.getPrice())
                .setQuantity(product.getQuantity());
    }

    public Product convertToProduct() {
        return new Product()
                .id(this.id)
                .idSeller(this.idSeller)
                .model(this.model)
                .name(this.name)
                .description(this.description)
                .price(this.price)
                .quantity(this.quantity);
    }

    public String getId() {
        return id;
    }

    public ProductDocument setId(String id) {
        this.id = id;
        return this;
    }

    public String getIdSeller() {
        return idSeller;
    }

    public ProductDocument setIdSeller(String idSeller) {
        this.idSeller = idSeller;
        return this;
    }

    public String getModel() {
        return model;
    }

    public ProductDocument setModel(String model) {
        this.model = model;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductDocument setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductDocument setDescription(String description) {
        this.description = description;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ProductDocument setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ProductDocument setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public ProductDocument setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public ProductDocument setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
        return this;
    }
}
