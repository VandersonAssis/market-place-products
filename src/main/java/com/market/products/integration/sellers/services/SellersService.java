package com.market.products.integration.sellers.services;

import com.market.products.model.Seller;

import java.util.Optional;

public interface SellersService {
    Optional<Seller> findById(String sellerId);
}
