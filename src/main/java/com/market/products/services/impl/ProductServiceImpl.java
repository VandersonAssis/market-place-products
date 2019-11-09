package com.market.products.services.impl;

import com.market.exceptions.custom.ResourceNotFoundException;
import com.market.products.documents.ProductDocument;
import com.market.products.documents.ProductLockDocument;
import com.market.products.model.Product;
import com.market.products.model.ProductLock;
import com.market.products.model.ProductUnlock;
import com.market.products.repositories.ProductLockRepository;
import com.market.products.repositories.ProductRepository;
import com.market.products.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductLockRepository productLockRepository;

    ProductServiceImpl(ProductRepository productRepository, ProductLockRepository productLockRepository) {
        this.productRepository = productRepository;
        this.productLockRepository = productLockRepository;
    }

    @Override
    public ProductDocument save(Product product) {
        return this.productRepository.save(ProductDocument.build(product));
    }

    @Override
    public List<Product> findByIdSeller(String idSeller) {
        List<ProductDocument> sellerProducts = this.productRepository.findByIdSeller(idSeller);

        if(!sellerProducts.isEmpty())
            return sellerProducts.stream().map(ProductDocument::convertToProduct).collect(Collectors.toList());
        else
            throw new ResourceNotFoundException();
    }

    @Override
    public void delete(String productId) {
        this.productRepository.deleteById(productId);
    }

    @Override
    public Optional<ProductLock> lockForSelling(ProductLock productLock) {
        ProductDocument productDocument = this.productRepository.countByIdGreaterThanEqual(productLock.getIdProduct(), 3);
        if(productDocument == null) return Optional.empty();
        productDocument.setQuantity(productDocument.getQuantity() - productLock.getQuantity());

        this.productRepository.save(productDocument);
        productLock = this.productLockRepository.save(ProductLockDocument.build(productLock)).convertToProductLock();

        return Optional.of(productLock);
    }

    @Override
    public boolean unlockForSelling(ProductUnlock productUnlock) {
        Optional<ProductLockDocument> optionalLockedProduct = this.productLockRepository.findById(productUnlock.getDocumentId());
        if(optionalLockedProduct.isEmpty()) return false;

        ProductLockDocument lockedProduct = optionalLockedProduct.get();
        ProductDocument product = this.productRepository.findById(lockedProduct.getProductId()).orElseThrow();

        product.setQuantity(product.getQuantity() + lockedProduct.getQuantity());
        this.productRepository.save(product);
        this.productLockRepository.delete(lockedProduct);

        return true;
    }

}
