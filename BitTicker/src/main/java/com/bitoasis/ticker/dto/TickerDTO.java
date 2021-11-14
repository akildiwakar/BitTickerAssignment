package com.bitoasis.ticker.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TickerDTO {


    @ApiModelProperty(value = "Ticker id")
    private Long id;

    @ApiModelProperty(value = "Price of last highest bid")
    @NotNull
    private BigDecimal bid;

    @ApiModelProperty(value = "Sum of the 25 highest bid sizes")
    @NotNull
    private BigDecimal bidSize;

    @ApiModelProperty(value = "Price of last lowest ask")
    @NotNull
    private BigDecimal ask;

    @ApiModelProperty(value = "Sum of the 25 lowest ask sizes")
    @NotNull
    private BigDecimal askSize;

    @ApiModelProperty(value = "Amount that the last price has changed since yesterday")
    @NotNull
    private BigDecimal dailyChange;

    @ApiModelProperty(value = "Relative price change since yesterday (*100 for percentage change)")
    @NotNull
    private BigDecimal dailyChangeRelative;

    @ApiModelProperty(value = "Price of the last trade")
    @NotNull
    private BigDecimal lastPrice;

    @ApiModelProperty(value = "Daily volume")
    @NotNull
    private BigDecimal volume;

    @ApiModelProperty(value = "Daily high")
    @NotNull
    private BigDecimal high;

    @ApiModelProperty(value = "Daily low")
    @NotNull
    private BigDecimal low;

    @ApiModelProperty(value = "Created Timestamp")
    private Instant createdTs;

    @ApiModelProperty(value = "Updated Timestamp")
    private Instant updatedTs;
}
