package com.market.products.repositories;

import com.market.products.documents.ProductLockDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductLockRepository extends MongoRepository<ProductLockDocument, String> {
}
