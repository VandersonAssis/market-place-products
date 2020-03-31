package com.market.products.services.impl;

import com.market.products.documents.ProductDocument;
import com.market.products.documents.ProductLockDocument;
import com.market.products.model.ProductLock;
import com.market.products.repositories.ProductLockRepository;
import com.market.products.repositories.ProductRepository;
import com.market.products.services.ProductLockService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductLockServiceImpl implements ProductLockService {
    private static final Logger log = LogManager.getLogger(ProductLockServiceImpl.class);

    @Autowired
    private ProductLockRepository productLockRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Optional<ProductLock> lockForSelling(ProductLock productLock) {
        log.info("Locking {} products for {} product id", productLock.getQuantity(), productLock.getIdProduct());
        ProductDocument productDocument = this.productRepository.countByIdGreaterThanEqual(productLock.getIdProduct(), productLock.getQuantity());

        if(productDocument == null) {
            log.warn("Product {} not found for the locking quantity of {}", productLock.getIdProduct(), productLock.getQuantity());
            return Optional.empty();
        }

        log.info("Product found with quantity of {}", productDocument.getQuantity());
        productDocument.setQuantity(productDocument.getQuantity() - productLock.getQuantity());
        this.productRepository.save(productDocument);
        productLock = this.productLockRepository.save(ProductLockDocument.build(productLock)).convertToProductLock();
        log.info("Locked successfully, product's new quantity is {}", productDocument.getQuantity());

        return Optional.of(productLock);
    }

    @Override
    public Optional<ProductLock> findById(String lockId) {
        log.info("Finding lock with {} lockId", lockId);
        return this.productLockRepository.findById(lockId).map(ProductLockDocument::convertToProductLock);
    }

    @Override
    public boolean unlockForSelling(String lockId) {
        log.info("{} lockId", lockId);
        Optional<ProductLockDocument> optionalLockedProduct = this.productLockRepository.findById(lockId);
        if(optionalLockedProduct.isEmpty()) {
            log.error("Product lock for {} id not found!", lockId);
            return false;
        }

        ProductLockDocument lockedProduct = optionalLockedProduct.get();
        ProductDocument product = this.productRepository.findById(lockedProduct.getProductId()).orElseThrow();

        log.info("Lock for {} id found. Locked quantity is {} and product's current quantity is {}", lockId, lockedProduct.getQuantity(), product.getQuantity());
        product.setQuantity(product.getQuantity() + lockedProduct.getQuantity());
        this.productRepository.save(product);
        this.productLockRepository.delete(lockedProduct);
        log.info("Product unlocked successfully. It's new quantity is {}", product.getQuantity());

        return true;
    }

    @Override
    public void deleteById(String lockId) {
        log.info("Deleting lock for {} lockId", lockId);
        this.productLockRepository.deleteById(lockId);
    }
}
