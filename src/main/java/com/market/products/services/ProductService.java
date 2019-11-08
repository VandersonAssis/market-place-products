package com.market.products.services;

import com.market.products.documents.ProductDocument;
import com.market.products.model.Product;
import com.market.products.model.ProductLock;

import java.util.List;

public interface ProductService {
    ProductDocument save(Product product);
    List<Product> findByIdSeller(String idSeller);
    void delete(String productId);
    boolean lockForSelling(ProductLock productLock);
}
