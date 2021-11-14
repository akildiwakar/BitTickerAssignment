package com.bitoasis.ticker.controller;

import com.bitoasis.ticker.TestSecurityConfig;
import com.bitoasis.ticker.dto.TickerDTO;
import com.bitoasis.ticker.dto.TickerResponse;
import com.bitoasis.ticker.exceptions.TickerNotFoundException;
import com.bitoasis.ticker.service.TickerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;

import static java.util.Arrays.asList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*@WebMvcTest(controllers = TickerController.class,
excludeAutoConfiguration = {
        UserDetailsServiceAutoConfiguration.class, SecurityAutoConfiguration.class
})*/
@AutoConfigureMockMvc()
@SpringBootTest(classes=TestSecurityConfig.class)
class TickerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TickerService tickerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "ticker", roles= {"USER","ADMIN"})
    void getAllTickers() throws Exception {

        TickerDTO tickerRequest1 = getTickerDTO();

        TickerDTO tickerRequest2 = getTickerDTO();

        Mockito.when(tickerService.getAllTickers()).thenReturn(asList(tickerRequest1, tickerRequest2));

        mockMvc.perform(get("/api/v1/tickers/all")
        		.with(SecurityMockMvcRequestPostProcessors.user("akil").roles("ADMIN","USER"))
        		.with(SecurityMockMvcRequestPostProcessors.csrf()))
               .andExpect(status().is(200))
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.size()", Matchers.is(2)))
               .andExpect(jsonPath("$[0].id", Matchers.is(1)))
               .andExpect(jsonPath("$[0].dailyChange", Matchers.is(-461)))
               .andExpect(jsonPath("$[0].bidSize", Matchers.is(800)))
               .andExpect(jsonPath("$[1].volume", Matchers.is(3412)))
               .andExpect(jsonPath("$[1].high", Matchers.is(64495)))
               .andExpect(jsonPath("$[1].low", Matchers.is(62255)));
    }


    @Test
    @WithMockUser(username = "ticker", roles= {"USER","ADMIN"})
    void testGetTicker() throws Exception {
        TickerDTO tickerRequest1 = getTickerDTO();
        Long id = 1L;

        Mockito.when(tickerService.getTicker(id)).thenReturn(tickerRequest1);

        mockMvc.perform(get("/api/v1/tickers/{id}", id))
               .andExpect(status().is(200))
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id", Matchers.is(1)))
               .andExpect(jsonPath("$.dailyChange", Matchers.is(-461)))
               .andExpect(jsonPath("$.bidSize", Matchers.is(800)));

    }

    @Test
    @WithMockUser(username = "ticker", roles= {"USER","ADMIN"})
    void testGetTickerException() throws Exception {
        Long id = 1L;

        Mockito.when(tickerService.getTicker(id)).thenThrow(TickerNotFoundException.class);

        mockMvc.perform(get("/api/v1/tickers/{id}", id))
               .andExpect(status().isNotFound());

    }

    @Test
    @WithMockUser(username = "ticker", roles= {"USER","ADMIN"})
    void testUpdateTicker() throws Exception {
        TickerDTO tickerRequest1 = getTickerDTO();
        Long id = 1L;

        Mockito.when(tickerService.updateTicker(tickerRequest1, id)).thenReturn(tickerRequest1);

        mockMvc.perform(put("/api/v1/tickers/{id}", id)
                       .content(objectMapper.writeValueAsString(tickerRequest1))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().is(200))
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id", Matchers.is(1)))
               .andExpect(jsonPath("$.dailyChange", Matchers.is(-461)))
               .andExpect(jsonPath("$.bidSize", Matchers.is(800)));

    }

    @Test
    @WithMockUser(username = "ticker", roles= {"USER","ADMIN"})
    void testGetAllTickersPaged() throws Exception {

        TickerDTO tickerRequest1 = getTickerDTO();
        TickerDTO tickerRequest2 = getTickerDTO();
        int pageNo = 1;
        int pageSize = 10;
        String sortBy = "bid";
        String sortDir = "asc";

        TickerResponse res = TickerResponse.builder().tickers(asList(tickerRequest1, tickerRequest2)).pageSize(10).build();

        Mockito.when(tickerService.getAllTickers(pageNo, pageSize, sortBy, sortDir))
               .thenReturn(res);

        mockMvc.perform(get("/api/v1/tickers"))
               .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(username = "ticker", roles= {"USER","ADMIN"})
    void testDeleteTicker() throws Exception {

        TickerDTO tickerRequest1 = getTickerDTO();
        Long id = 1L;

        Mockito.doNothing().when(tickerService).deleteTickerById(id);

        mockMvc.perform(delete("/api/v1/tickers/{id}", id))
               .andExpect(status().is(200))
               .andExpect(jsonPath("$", Matchers.is("Ticker Deleted Successfully")));

    }

    @Test
    @WithMockUser(username = "ticker", roles= {"USER","ADMIN"})
    void testDeleteAllTickers() throws Exception {

        Mockito.doNothing().when(tickerService).deleteAll();

        mockMvc.perform(delete("/api/v1/tickers/all"))
               .andExpect(status().is(200))
               .andExpect(jsonPath("$", Matchers.is("All Tickers Deleted Successfully")));
    }

    TickerDTO getTickerDTO() {
        return TickerDTO.builder()
                        .id(1L)
                        .bid(BigDecimal.valueOf(63534))
                        .bidSize(BigDecimal.valueOf(800))
                        .ask(BigDecimal.valueOf(600l))
                        .askSize(BigDecimal.valueOf(900))
                        .dailyChange(BigDecimal.valueOf(-461))
                        .dailyChangeRelative(BigDecimal.valueOf(-1))
                        .lastPrice(BigDecimal.valueOf(63539))
                        .volume(BigDecimal.valueOf(3412))
                        .high(BigDecimal.valueOf(64495))
                        .low(BigDecimal.valueOf(62255))
                        .createdTs(Instant.now())
                        .updatedTs(Instant.now()).build();
    }


}