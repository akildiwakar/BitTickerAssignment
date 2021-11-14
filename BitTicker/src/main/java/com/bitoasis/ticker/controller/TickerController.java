package com.bitoasis.ticker.controller;

import com.bitoasis.ticker.dto.TickerDTO;
import com.bitoasis.ticker.dto.TickerResponse;
import com.bitoasis.ticker.service.TickerService;
import com.bitoasis.ticker.util.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller API to perform operations on Tickers
 *
 * @author Akil Diwakar
 */
@Api(value = "APIs for Ticker")
@RestController
@RequestMapping("/api/v1/tickers")
@AllArgsConstructor
@Log4j2
public class TickerController {

    private final TickerService tickerService;

    @ApiOperation(value = "Get All Tickers - REST API with Pagination and Sorting")
    @GetMapping
    public TickerResponse getAllTickersPaged(
            @ApiParam(name =  "pageNo", type = "Integer", value = "Page No to be fetched", example = "2", required = false)
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,

            @ApiParam(name =  "pageSize", type = "Integer", value = "No Of records in each page", example = "20", required = false)
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,

            @ApiParam(name =  "sortBy", type = "String", value = "Field on which the Tickers should be sorted", example = "id", required = false)
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,

            @ApiParam(name =  "sortDir", type = "String", value = "Sorting by Ascending (asc) or Descending (desc)", example = "asc", required = false)
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return tickerService.getAllTickers(pageNo, pageSize, sortBy, sortDir);
    }

    @ApiOperation(value = "Get Ticker By Id - REST API")
    @GetMapping("/{id}")
    public ResponseEntity<TickerDTO> getTicker(
            @ApiParam(name =  "id", type = "Long", value = "Id of the Ticker", example = "123", required = true)
            @PathVariable Long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tickerService.getTicker(id));
    }

    @ApiOperation(value = "Update Ticker By Id - REST API")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TickerDTO> updateTicker(
            @ApiParam(name =  "tickerDTO", type = "TickerDTO", value = "Contains the Data to be Updated",required = true)
            @Valid @RequestBody TickerDTO tickerDTO,

            @ApiParam(name =  "id", type = "Long", value = "Id of the Ticker", example = "123", required = true)
            @PathVariable(name = "id") long id
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tickerService.updateTicker(tickerDTO, id));
    }

    @ApiOperation(value = "Delete Ticker By Id - REST API")
    @ApiParam(name =  "id", type = "Long", value = "Id of the Ticker", example = "123", required = true)
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteTicker(
            @ApiParam(name =  "id", type = "Long", value = "Id of the Ticker", example = "123", required = true)
            @PathVariable(name = "id") long id
    ) {
        tickerService.deleteTickerById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Ticker Deleted Successfully");
    }

    @ApiOperation(value = "Get All Tickers - REST API (Fetches the complete List of Tickers)")
    @GetMapping("/all")
    public ResponseEntity<List<TickerDTO>> getAllTickers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tickerService.getAllTickers());
    }

    @ApiOperation(value = "Delete ALL Tickers REST API")
    @DeleteMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAllTickers() {
        tickerService.deleteAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("All Tickers Deleted Successfully");
    }



}
