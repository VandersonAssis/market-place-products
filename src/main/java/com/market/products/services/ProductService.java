package com.market.products.services;

import com.market.products.documents.ProductDocument;
import com.market.products.model.Product;

import java.util.List;

public interface ProductService {
    ProductDocument save(Product product);
    ProductDocument edit(Product product);
    List<Product> findByIdSeller(String idSeller);
    void delete(String productId);
    void deleteProducts(String sellerId);
}
