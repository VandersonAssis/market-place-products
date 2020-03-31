package com.market.products.repositories;

import com.market.products.documents.ProductDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<ProductDocument, String> {
    @Query("{ 'id': '?0', 'quantity': { $gte: ?1 } }")
    ProductDocument countByIdGreaterThanEqual(String id, int quantity);
    List<ProductDocument> findByIdSeller(String idSeller);
    void deleteByIdSeller(String sellerId);
}