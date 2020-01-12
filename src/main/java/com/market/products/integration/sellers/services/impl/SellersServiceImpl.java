package com.market.products.integration.sellers.services.impl;

import com.market.exceptions.custom.BaseHttpException;
import com.market.exceptions.exceptionhandlers.ApiError;
import com.market.products.integration.sellers.SellersApiProxy;
import com.market.products.integration.sellers.services.SellersService;
import com.market.products.model.Seller;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class SellersServiceImpl implements SellersService {
    @Autowired
    private SellersApiProxy sellersApiProxy;

    @Override
    public Optional<Seller> findById(String sellerId) {
        try {
            ResponseEntity<Seller> response = this.sellersApiProxy.findById(sellerId);
            return Optional.ofNullable(response.getBody());
        } catch(FeignException ex) {
            if(ex.status() == NOT_FOUND.value())
                return Optional.empty();
            else
                throw new BaseHttpException(new ApiError(INTERNAL_SERVER_ERROR, "Error in SellersServiceImpl.findById"));
        }
    }
}
