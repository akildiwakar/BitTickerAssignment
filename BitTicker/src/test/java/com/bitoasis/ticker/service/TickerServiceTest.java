package com.bitoasis.ticker.service;

import com.bitoasis.ticker.client.TickerClientService;
import com.bitoasis.ticker.dto.TickerDTO;
import com.bitoasis.ticker.entity.Ticker;
import com.bitoasis.ticker.mapper.TickerMapper;
import com.bitoasis.ticker.repository.TickerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TickerServiceTest {

    @Mock
    private TickerClientService tickerClientService;

    @Mock
    private TickerRepository tickerRepo;

    @Mock
    private TickerMapper tickerMapper;

    @InjectMocks
    private TickerService tickerService;

    @Captor
    private ArgumentCaptor<Ticker> tickerArgumentCaptor;

    @Test
    public void testScheduledTrigger() {

        Ticker expectedTicker = getTicker();

        ArrayList clientResponse = getClientData();

        Mockito.when(tickerClientService.fetchTickerData()).thenReturn(clientResponse);
        Mockito.when(tickerMapper.map(clientResponse)).thenReturn(expectedTicker);
        Mockito.when(tickerRepo.save(expectedTicker)).thenReturn(expectedTicker);

        tickerService.scheduledTrigger();

        Mockito.verify(tickerRepo, Mockito.times(1)).save(tickerArgumentCaptor.capture());

        Assertions.assertThat(tickerArgumentCaptor.getValue().getId()).isEqualTo(1L);
        Assertions.assertThat(tickerArgumentCaptor.getValue().getAsk()).isEqualTo(BigDecimal.valueOf(600l));
    }

    @Test
    public void testGetAllTickers() {

        Ticker ticker1 = getTicker();
        Ticker ticker2 = getTicker();

        TickerDTO tickRes1 = getTickerDTO();
        TickerDTO tickRes2 = getTickerDTO();

        List<TickerDTO> expectedTickerResponse = List.of(tickRes1, tickRes2);

        Mockito.when(tickerRepo.findAll()).thenReturn(Arrays.asList(ticker1, ticker2));
        Mockito.when(tickerMapper.mapToDto(ticker1)).thenReturn(tickRes1);

        List<TickerDTO> actualResponse = tickerService.getAllTickers();

        Assertions.assertThat(actualResponse.get(0).getId()).isEqualTo(expectedTickerResponse.get(0).getId());
        Assertions.assertThat(actualResponse.get(0).getBidSize()).isEqualTo(expectedTickerResponse.get(0).getBidSize());

    }


    @Test
    public void testGetTicker() {

        Ticker ticker = getTicker();

        TickerDTO expectedTickerResponse = getTickerDTO();

        Mockito.when(tickerRepo.findById(1L)).thenReturn(Optional.of(ticker));
        Mockito.when(tickerMapper.mapToDto(Mockito.any(Ticker.class))).thenReturn(expectedTickerResponse);

        TickerDTO actualResponse = tickerService.getTicker(1L);

        Assertions.assertThat(actualResponse.getId()).isEqualTo(expectedTickerResponse.getId());
        Assertions.assertThat(actualResponse.getBidSize()).isEqualTo(expectedTickerResponse.getBidSize());

    }

    @Test
    public void testUpdateTicker() {

        TickerDTO tickerDTO = getTickerDTO();
        Ticker ticker = getTicker();
        Long id = 1l;

        Mockito.when(tickerRepo.findById(id)).thenReturn(Optional.of(ticker));
        Mockito.when(tickerMapper.map(tickerDTO, id, ticker.getCreatedTs())).thenReturn(ticker);
        Mockito.when(tickerRepo.save(ticker)).thenReturn(ticker);

        TickerDTO actualResponse = tickerService.updateTicker(tickerDTO, id);

        Mockito.verify(tickerRepo, Mockito.times(1)).save(tickerArgumentCaptor.capture());
        Assertions.assertThat(tickerArgumentCaptor.getValue().getId().equals(1L));
        Assertions.assertThat(tickerArgumentCaptor.getValue().getAsk()).isEqualTo(BigDecimal.valueOf(600l));


    }

    @Test
    public void testDeleteTickerById() {

        Ticker ticker = getTicker();
        Long id = 1l;

        Mockito.when(tickerRepo.findById(id)).thenReturn(Optional.of(ticker));
        Mockito.doNothing().when(tickerRepo).delete(ticker);

        tickerService.deleteTickerById(id);

        Mockito.verify(tickerRepo, Mockito.times(1)).delete(ticker);

    }

    @Test
    public void deleteAll() {

        tickerService.deleteAll();
        Mockito.verify(tickerRepo, Mockito.times(1)).deleteAllWithQuery();
    }

    Ticker getTicker() {
        return Ticker.builder()
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

    ArrayList<BigDecimal> getClientData() {
       return new ArrayList(List.of(64751, 2.1640621700000002, 64752, 9.59824815, 1039, 0.0163, 64751, 2090.16012354, 65311, 63405));
    }
}
