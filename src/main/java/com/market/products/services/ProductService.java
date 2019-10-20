package com.market.products.services;

import com.market.products.documents.ProductDocument;
import com.market.products.model.Product;

import java.util.List;

public interface ProductService {
    ProductDocument save(Product product);
    List<Product> findByIdSeller(String idSeller);
    boolean update(Product product);
    boolean delete(String id);
}
