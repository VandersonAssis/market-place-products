package com.market.products.integration.sellers;

import com.market.products.model.Seller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

//TODO it seems like that this url has to be the kubernetes service's
//TODO fix the tests

public interface SellersApiProxy {
    ResponseEntity<Seller> findById(@PathVariable("idSellerddddd") String idSeller);
}
