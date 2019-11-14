package com.market.products.services;

import com.market.products.model.ProductLock;

import java.util.Optional;

public interface ProductLockService {
    Optional<ProductLock> lockForSelling(ProductLock productLock);
    Optional<ProductLock> findById(String lockId);
    boolean unlockForSelling(String lockId);
    void deleteById(String lockId);
}
