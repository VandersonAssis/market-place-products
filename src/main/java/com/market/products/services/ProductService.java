package com.market.products.services;

import com.market.products.documents.ProductDocument;
import com.market.products.model.Product;
import com.market.products.model.ProductLock;
import com.market.products.model.ProductUnlock;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductDocument save(Product product);
    List<Product> findByIdSeller(String idSeller);
    void delete(String productId);
    Optional<ProductLock> lockForSelling(ProductLock productLock);
    boolean unlockForSelling(ProductUnlock productLockToggle);
}
