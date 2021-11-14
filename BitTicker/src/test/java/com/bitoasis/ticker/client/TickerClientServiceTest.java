package com.bitoasis.ticker.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TickerClientServiceTest {

    @InjectMocks
    private TickerClientService tickerClientService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void fetchTickerData() {

        ResponseEntity mockResponse = new ResponseEntity(Arrays.asList(1,2,3), HttpStatus.OK);

        Mockito.lenient().when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<List>>any(),
                ArgumentMatchers.<ParameterizedTypeReference<List<Object>>>any())
        ).thenReturn(mockResponse);

        assertNotNull(mockResponse);

    }
}