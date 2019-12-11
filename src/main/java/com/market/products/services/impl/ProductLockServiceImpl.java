package com.market.products.services.impl;

import com.market.products.documents.ProductDocument;
import com.market.products.documents.ProductLockDocument;
import com.market.products.model.ProductLock;
import com.market.products.repositories.ProductLockRepository;
import com.market.products.repositories.ProductRepository;
import com.market.products.services.ProductLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductLockServiceImpl implements ProductLockService {
    @Autowired
    private ProductLockRepository productLockRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Optional<ProductLock> lockForSelling(ProductLock productLock) {
        ProductDocument productDocument = this.productRepository.countByIdGreaterThanEqual(productLock.getIdProduct(), productLock.getQuantity());
        if(productDocument == null) return Optional.empty();

        productDocument.setQuantity(productDocument.getQuantity() - productLock.getQuantity());
        this.productRepository.save(productDocument);
        productLock = this.productLockRepository.save(ProductLockDocument.build(productLock)).convertToProductLock();

        return Optional.of(productLock);
    }

    @Override
    public Optional<ProductLock> findById(String lockId) {
        return this.productLockRepository.findById(lockId).map(ProductLockDocument::convertToProductLock);
    }

    @Override
    public boolean unlockForSelling(String lockId) {
        Optional<ProductLockDocument> optionalLockedProduct = this.productLockRepository.findById(lockId);
        if(optionalLockedProduct.isEmpty()) return false;

        ProductLockDocument lockedProduct = optionalLockedProduct.get();
        ProductDocument product = this.productRepository.findById(lockedProduct.getProductId()).orElseThrow();

        product.setQuantity(product.getQuantity() + lockedProduct.getQuantity());
        this.productRepository.save(product);
        this.productLockRepository.delete(lockedProduct);

        return true;
    }

    @Override
    public void deleteById(String lockId) {
        this.productLockRepository.deleteById(lockId);
    }
}
