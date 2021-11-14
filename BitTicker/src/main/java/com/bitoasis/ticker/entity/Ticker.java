package com.bitoasis.ticker.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Table(name = "TICKER_BTCUSD")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ticker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tickerId", nullable = false, updatable = false)
    private Long id;

    private BigDecimal bid;
    private BigDecimal bidSize;
    private BigDecimal ask;
    private BigDecimal askSize;
    private BigDecimal dailyChange;
    private BigDecimal dailyChangeRelative;
    private BigDecimal lastPrice;
    private BigDecimal volume;
    private BigDecimal high;
    private BigDecimal low;

    private Instant createdTs;
    private Instant updatedTs;
}
