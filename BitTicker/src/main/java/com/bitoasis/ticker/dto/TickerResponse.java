package com.bitoasis.ticker.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TickerResponse {

    @ApiModelProperty(value = "Page No of the List of Tickers")
    private int pageNo;

    @ApiModelProperty(value = "No of Tickers in each page")
    private int pageSize;

    @ApiModelProperty(value = "The Total number of Tickers that exist")
    private long totalElements;

    @ApiModelProperty(value = "The Total number of Pages that exist")
    private int totalPages;

    @ApiModelProperty(value = "Is the Current page the 'Last and final page'")
    private boolean last;

    @ApiModelProperty(value = "List of Tickers")
    private List<TickerDTO> tickers;

}
