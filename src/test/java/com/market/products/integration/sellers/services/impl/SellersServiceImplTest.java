package com.market.products.integration.sellers.services.impl;

import com.market.products.exceptions.custom.BaseHttpException;
import com.market.products.integration.sellers.SellersApiProxy;
import com.market.products.integration.sellers.services.SellersService;
import com.market.products.model.Seller;
import feign.FeignException;
import feign.Request;
import org.aspectj.util.Reflection;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.market.products.TestDataBuilder.buildTestSeller;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
public class SellersServiceImplTest {

    @Mock
    private SellersApiProxy sellersApiProxy;

    @Mock
    private MessageSource msg;

    private SellersService sellersService;

    @Before
    public void setUp() {
        this.sellersService = new SellersServiceImpl();
        ReflectionTestUtils.setField(this.sellersService, "sellersApiProxy", this.sellersApiProxy);
        ReflectionTestUtils.setField(this.sellersService, "msg", this.msg);
        when(this.msg.getMessage(anyString(), any(), any())).thenReturn("Test error message");
    }

    @Ignore
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    public void shouldFindSellersByIdSuccessfully() {
        when(this.sellersApiProxy.findById(anyString())).thenReturn(new ResponseEntity<>(buildTestSeller(), OK));

        Optional<Seller> seller = this.sellersService.findById("seller_test_id");
        assertNotNull(seller.get());
    }

    @Ignore
    @SuppressWarnings("deprecation")
    @Test
    public void whenSellerNotFoundThenEmptyOptional() {
        Map<String, Collection<String>> headers = new HashMap<>();
        Request request = Request.create("GET", "http://localhost:8080/test", headers, new byte[10], Charset.defaultCharset());
        when(this.sellersApiProxy.findById(anyString())).thenThrow(new FeignException.NotFound("Test exception", request, new byte[18]));
        Optional<Seller> seller = this.sellersService.findById("test_seller_id");

        assertTrue(seller.isEmpty());
    }

    @Ignore
    @SuppressWarnings("deprecation")
    @Test
    public void shouldConvertExceptionIntoBaseHttpException() {
        Map<String, Collection<String>> headers = new HashMap<>();
        Request request = Request.create("GET", "http://localhost:8080/test", headers, new byte[10], Charset.defaultCharset());
        when(this.sellersApiProxy.findById(anyString())).thenThrow(new FeignException.BadRequest("Test exception", request, new byte[18]));
        assertThrows(BaseHttpException.class, () -> this.sellersService.findById("test_seller_id"));
    }
}