package com.market.products.controllers;

import com.market.exceptions.custom.ResourceNotFoundException;
import com.market.products.api.ProductsApi;
import com.market.products.model.Product;
import com.market.products.model.ProductListResponse;
import com.market.products.model.ProductLock;
import com.market.products.services.ProductLockService;
import com.market.products.services.ProductService;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestController
public class ProductController extends BaseController implements ProductsApi {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductLockService productLockService;

    @Override
    public ResponseEntity<Product> saveProduct(@Valid Product product) {
        Product savedProduct = this.productService.save(product).convertToProduct();
        return new ResponseEntity<>(savedProduct, CREATED);
    }

    @Override
    public ResponseEntity<Void> updateProduct(@Valid Product product) {
        this.productService.save(product);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @Override
    public ResponseEntity<ProductListResponse> listProductsBySeller(String idSeller) {
        List<Product> products = this.productService.findByIdSeller(idSeller);
        ProductListResponse productListResponse = new ProductListResponse();
        productListResponse.addAll(products);

        return new ResponseEntity<>(productListResponse, OK);
    }

    @Override
    public ResponseEntity<Void> deleteProduct(String idProduct) {
        this.productService.delete(idProduct);
        return new ResponseEntity<>(NO_CONTENT);
    }

    @Override
    public ResponseEntity<ProductLock> lockProductQuantity(@Valid ProductLock productLock) {
        Optional<ProductLock> lockedProduct = this.productLockService.lockForSelling(productLock);

        return lockedProduct.map(lock -> new ResponseEntity<>(lock, OK))
                .orElseGet(() -> new ResponseEntity<>(BAD_REQUEST));
    }

    @Override
    public ResponseEntity<ProductLock> getProductLock(String idLock) {
        return this.productLockService.findById(idLock).map(lock -> new ResponseEntity<>(lock, OK))
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public ResponseEntity<Void> unlockProductQuantity(String lockId) {
        if(this.productLockService.unlockForSelling(lockId))
            return new ResponseEntity<>(OK);
        else
            throw new ResourceNotFoundException();
    }

    @Override
    public ResponseEntity<Void> deleteLock(String lockId) {
        this.productLockService.deleteById(lockId);
        return new ResponseEntity<>(OK);
    }

    @Override
    public ResponseEntity<String> getCurrentVersion() {
        return new ResponseEntity<>(this.productService.findSystemCurrentVersion(), OK);
    }
}
