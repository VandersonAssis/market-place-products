package com.market.products.integration.sellers.services.impl;

import com.market.products.exceptions.custom.BaseHttpException;
import com.market.products.exceptions.exceptionhandlers.ApiError;
import com.market.products.integration.sellers.SellersApiProxy;
import com.market.products.integration.sellers.services.SellersService;
import com.market.products.model.Seller;
import feign.FeignException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class SellersServiceImpl implements SellersService {
    private static final Logger log = LogManager.getLogger(SellersServiceImpl.class);

    @Autowired
    private MessageSource msg;

//    @Autowired
//    private SellersApiProxy sellersApiProxy;

    @Override
    public Optional<Seller> findById(String sellerId) {
        return Optional.empty();

//        try {
//            log.info("{} begin", sellerId);
//            ResponseEntity<Seller> response = this.sellersApiProxy.findById(sellerId);
//            log.info("Returning response");
//
//            return Optional.ofNullable(response.getBody());
//        } catch(FeignException ex) {
//            if(ex.status() == NOT_FOUND.value()) {
//                log.info("{} seller not found", sellerId);
//                return Optional.empty();
//            } else {
//                log.error("Error while trying to find {} seller", sellerId, ex);
//                throw new BaseHttpException(new ApiError(INTERNAL_SERVER_ERROR, this.msg.getMessage("internal.server.error", null, Locale.getDefault())));
//            }
//        }
    }
}
