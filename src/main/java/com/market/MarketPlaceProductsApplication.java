package com.market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@EnableFeignClients("com.market.products")
@SpringBootApplication
public class MarketPlaceProductsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketPlaceProductsApplication.class, args);
	}

}
