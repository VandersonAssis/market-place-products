package com.market.products.repositories;

import com.market.products.documents.ProductDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<ProductDocument, String> {
    List<ProductDocument> findByIdSeller(String idSeller);
}