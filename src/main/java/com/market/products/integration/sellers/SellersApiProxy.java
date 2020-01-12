package com.market.products.integration.sellers;

import com.market.products.model.Seller;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${sellers.spring.application.name}")
@RibbonClient(name = "${sellers.spring.application.name}")
public interface SellersApiProxy {
    @GetMapping("/marketplace/api/v1/sellers/{idSeller}")
    ResponseEntity<Seller> findById(@PathVariable("idSeller") String idSeller);
}
