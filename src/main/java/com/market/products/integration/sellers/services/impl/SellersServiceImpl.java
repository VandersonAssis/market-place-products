package com.market.products.integration.sellers.services.impl;
import com.market.products.exceptions.custom.BaseHttpException;
import com.market.products.exceptions.exceptionhandlers.ApiError;
import com.market.products.integration.sellers.SellersApiProxy;
import com.market.products.integration.sellers.services.SellersService;
import com.market.products.model.Seller;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Optional<Seller> findById(String sellerId) {
        try {
            ResponseEntity<Seller> response = this.sellersApiProxy.findById(sellerId);

            logger.info("{}", response);

            return Optional.ofNullable(response.getBody());
        } catch(FeignException ex) {
            if(ex.status() == NOT_FOUND.value()) {
                return Optional.empty();
            } else {
                logger.error("Error while trying to find seller by id", ex);
                throw new BaseHttpException(new ApiError(INTERNAL_SERVER_ERROR, "Error in SellersServiceImpl.findById"));
            }
        }
    }
}
