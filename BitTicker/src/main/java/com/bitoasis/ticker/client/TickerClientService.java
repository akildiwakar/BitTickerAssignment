package com.bitoasis.ticker.client;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Client to Fetch Ticker Data
 *
 * @author Akil Diwakar
 */
@Component
@Log4j2
public class TickerClientService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${tickerUrl}")
    private String tickerUrl;

    /***
     * Fetches data for a Ticker and Returns the data of a Ticker as a List of its fields
     * @return List<BigDecimal>
     */
    public List<BigDecimal> fetchTickerData(){
        try {
            ResponseEntity<List<BigDecimal>> response = restTemplate.exchange(tickerUrl,
                    HttpMethod.GET,
                    prepareHeaders(),
                    new ParameterizedTypeReference<List<BigDecimal>>() {
                    });

            //log.debug("response : {}", response);

            return response.getBody();
        }catch (Exception e){
            throw new RestClientException(e.getMessage());
        }
    }

    private HttpEntity<Object> prepareHeaders() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return new HttpEntity<>(requestHeaders);
    }

}
