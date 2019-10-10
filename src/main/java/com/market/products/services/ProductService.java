package com.market.products.services;

import com.market.products.documents.ProductDocument;
import com.market.products.model.Product;

public interface ProductService {
    ProductDocument save(Product productDocument);
    boolean update(Product productDocument);
    boolean delete(String id);
}
