package com.bitoasis.ticker.mapper;

import com.bitoasis.ticker.dto.TickerDTO;
import com.bitoasis.ticker.dto.TickerResponse;
import com.bitoasis.ticker.entity.Ticker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TickerMapper {

    @Mapping(target = "createdTs", source = "createdTs")
    @Mapping(target = "updatedTs", source = "updatedTs")
    TickerDTO mapToDto(Ticker ticker);

    @Mapping(target = "id", source = "tickerId")
    @Mapping(target = "updatedTs", expression = "java(java.time.Instant.now())")
    @Mapping(target = "createdTs", source = "created")
    Ticker map(TickerDTO tickerDTO, long tickerId, Instant created);

    @Mapping(target = "tickers", expression = "java(mapTickers(content))")
    @Mapping(target = "pageNo", expression = "java(tickers.getNumber())")
    @Mapping(target = "pageSize", expression = "java(tickers.getSize())")
    @Mapping(target = "totalElements", expression = "java(tickers.getTotalElements())")
    @Mapping(target = "totalPages", expression = "java(tickers.getTotalPages())")
    @Mapping(target = "last", expression = "java(tickers.isLast())")
    TickerResponse mapToResponse(Object content, Page<Ticker> tickers);

    default List<TickerDTO> mapTickers(Object content) {
        return (List<TickerDTO>) content;
    }

    default Ticker map(List<BigDecimal> list) {
        if (list == null) {
            return null;
        }

        return Ticker.builder()
                     .bid(list.get(0))
                     .bidSize(list.get(1))
                     .ask(list.get(2))
                     .askSize(list.get(3))
                     .dailyChange(list.get(4))
                     .dailyChangeRelative(list.get(5))
                     .lastPrice(list.get(6))
                     .volume(list.get(7))
                     .high(list.get(8))
                     .low(list.get(9))
                     .createdTs(java.time.Instant.now())
                     .build();
    }

}
