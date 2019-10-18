package com.market.products.services;

import com.market.products.documents.ProductDocument;
import com.market.products.model.Product;

import java.util.List;

public interface ProductService {
    ProductDocument save(Product productDocument);
    List<Product> findByIdSeller(String idSeller);
    boolean update(Product productDocument);
    boolean delete(String id);
}
