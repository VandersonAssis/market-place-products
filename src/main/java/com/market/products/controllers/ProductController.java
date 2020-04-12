package com.market.products.controllers;

import com.market.products.api.ProductsApi;
import com.market.products.exceptions.custom.BaseHttpException;
import com.market.products.exceptions.exceptionhandlers.ApiError;
import com.market.products.model.Product;
import com.market.products.model.ProductListResponse;
import com.market.products.model.ProductLock;
import com.market.products.services.ProductLockService;
import com.market.products.services.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
public class ProductController extends BaseController implements ProductsApi {
    private static final Logger log = LogManager.getLogger(ProductController.class);

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductLockService productLockService;

    @Override
    public ResponseEntity<Product> saveProduct(@Valid Product product) {
        log.info("name {} begin", product.getName());
        product.setId(null);
        Product savedProduct = this.productService.save(product).convertToProduct();
        log.info("id {} product saved", savedProduct.getId());
        return new ResponseEntity<>(savedProduct, CREATED);
    }

    @Override
    public ResponseEntity<Void> updateProduct(@Valid Product product) {
        log.info("name {} begin", product.getName());
        this.productService.edit(product);
        log.info("id {} product updated", product.getId());
        return new ResponseEntity<>(NO_CONTENT);
    }

    @Override
    public ResponseEntity listProductsBySeller(String idSeller) {
        log.info("id {} begin", idSeller);

        List<Product> products = this.productService.findByIdSeller(idSeller);
        ProductListResponse productListResponse = new ProductListResponse();
        productListResponse.addAll(products);

        log.info("id {} returning ok with data", idSeller);
        return new ResponseEntity<>(productListResponse, OK);
    }

    @Override
    public ResponseEntity<Void> deleteProduct(String idProduct) {
        log.info("id {} begin", idProduct);
        this.productService.delete(idProduct);
        log.info("id {} product deleted", idProduct);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @Override
    public ResponseEntity<ProductLock> lockProductQuantity(@Valid ProductLock productLock) {
        log.info("id {} and quantity {} begin", productLock.getIdProduct(), productLock.getQuantity());

        if(productLock.getQuantity() <= 0) {
            log.error("Lock quantity of {} is invalid. Returning bad request response", productLock.getQuantity());
            throw new BaseHttpException(new ApiError(BAD_REQUEST, this.messageSource.getMessage("invalid.lock.quantity", null, Locale.getDefault())));
        }

        Optional<ProductLock> lockedProduct = this.productLockService.lockForSelling(productLock);
        log.info("Handling product lock return");
        return lockedProduct.map(lock -> new ResponseEntity<>(lock, OK))
                .orElseThrow(() -> new BaseHttpException(new ApiError(BAD_REQUEST, this.messageSource.getMessage("couldnt.lock.product",
                        new Object[] {productLock.getIdProduct()}, Locale.getDefault()))));
    }

    @Override
    public ResponseEntity<ProductLock> getProductLock(String idLock) {
        log.info("id {} begin", idLock);

        return this.productLockService.findById(idLock).map(lock -> new ResponseEntity<>(lock, OK))
                .orElseThrow(() -> new BaseHttpException(new ApiError(NOT_FOUND, this.messageSource.getMessage("product.lock.not.found", null, Locale.getDefault()))));
    }

    @Override
    public ResponseEntity<Void> unlockProductQuantity(String lockId) {
        log.info("id {} begin", lockId);

        if(this.productLockService.unlockForSelling(lockId)) {
            log.info("id {} unlocked", lockId);
            return new ResponseEntity<>(OK);
        }
        else {
            log.error("Unable to unlock id {}, returning not found", lockId);
            throw new BaseHttpException(new ApiError(NOT_FOUND, this.messageSource.getMessage("product.lock.not.found", null, Locale.getDefault())));
        }
    }

    @Override
    public ResponseEntity<Void> deleteLock(String lockId) {
        log.info("id {} begin", lockId);
        this.productLockService.deleteById(lockId);
        log.info("id {} deleted", lockId);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> deleteProducts(String idSeller) {
        log.info("idSeller {} begin", idSeller);
        this.productService.deleteProducts(idSeller);
        log.info("idSeller {} deleted", idSeller);
        return new ResponseEntity<>(NO_CONTENT);
    }
}
