package com.bitoasis.ticker.service;

import com.bitoasis.ticker.client.TickerClientService;
import com.bitoasis.ticker.dto.TickerDTO;
import com.bitoasis.ticker.dto.TickerResponse;
import com.bitoasis.ticker.entity.Ticker;
import com.bitoasis.ticker.exceptions.TickerNotFoundException;
import com.bitoasis.ticker.mapper.TickerMapper;
import com.bitoasis.ticker.repository.TickerRepository;
import com.bitoasis.ticker.util.AppConstants;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for performing Ticker related operations
 *
 * @author Akil Diwakar
 */
@Service
@AllArgsConstructor
@Log4j2
public class TickerService {

    private final TickerClientService tickerClientService;

    private final TickerMapper tickerMapper;

    private final TickerRepository tickerRepo;

    /***
     * Fetches Tickers from an API at scheduled intervals
     * and persists them into the database
     */
    @Transactional
    public void scheduledTrigger() {
        try {
            Ticker save = tickerRepo.save(tickerMapper.map(tickerClientService.fetchTickerData()));

            //commented logs because it generates too much noise in the logs
            //log.debug("Scheduler response saved successfully : {}", save);
            //log.info("Scheduled Trigger End");
        } catch (Exception e) {
            log.error("Scheduler failed to execute - {}", e.getMessage());
        }
    }

    /***
     * Returns all instances of tickers
     * @return List<TickerDTO> return a list of TickerDTO objects
     */
    @Transactional(readOnly = true)
    public List<TickerDTO> getAllTickers() {

        return tickerRepo.findAll()
                         .stream()
                         .map(tickerMapper::mapToDto)
                         .collect(Collectors.toList());
    }

    /***
     * Returns all instances of the ticker based on the parameters passed
     * @param pageNo page to be returned (Defaults to 0)
     * @param pageSize size of each page (Defaults to 10)
     * @param sortBy field on which to sort (Defaults to id)
     * @param sortDir asc or desc (Defaults to asc)
     * @return TickerResponse contains a list of tickers along with their parameters
     */
    @Transactional(readOnly = true)
    public TickerResponse getAllTickers(int pageNo, int pageSize, String sortBy, String sortDir) {

        String sortedBy = AppConstants.SORT_BY_PARAMETERS.contains(sortBy) ? sortBy : AppConstants.DEFAULT_SORT_BY;
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortedBy).ascending()
                : Sort.by(sortedBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Ticker> tickers = tickerRepo.findAll(pageable);

        // get content for page object
        List<Ticker> listOfTickers = tickers.getContent();

        List<TickerDTO> content = listOfTickers.stream()
                                               .map(tickerMapper::mapToDto)
                                               .collect(Collectors.toList());

        return tickerMapper.mapToResponse(content, tickers);
    }


    /**
     *Retrieves a Ticker by its id
     * @param id id of the ticker
     * @return the tickerDTO object for the given id
     */
    public TickerDTO getTicker(Long id) {
        Ticker ticker = tickerRepo.findById(id)
                                  .orElseThrow(() -> new TickerNotFoundException("No Ticker found with ID - " + id));
        return tickerMapper.mapToDto(ticker);
    }

    /**
     * Updates a Ticker based on the id and TickerDTO object passed as input
     * Throws TickerNotFound Exception if the id is not present in the Database
     * @param tickerDTO Ticker to be updated
     * @param id id of the Ticker to be updated
     * @return TickerDTO object that was updated
     */
    public TickerDTO updateTicker(TickerDTO tickerDTO, long id) {
        Ticker ticker = tickerRepo.findById(id)
                                  .orElseThrow(() -> new TickerNotFoundException("No Ticker found with ID - " + id));

        Ticker updatedTicker = tickerRepo.save(tickerMapper.map(tickerDTO, id,ticker.getCreatedTs()));

        return tickerMapper.mapToDto(updatedTicker);
    }

    /**
     * Deletes the instance of Ticker based on the id passed
     * Throws TickerNotFound Exception if the id is not present in the Database
     * @param id id of the Ticker to be deleted
     */
    public void deleteTickerById(long id) {

        Ticker ticker = tickerRepo.findById(id)
                                  .orElseThrow(() -> new TickerNotFoundException("No Ticker found with ID - " + id));
        tickerRepo.delete(ticker);
    }

    /**
     * Deletes all Tickers
     */
    @Transactional
    public void deleteAll() {
            tickerRepo.deleteAllWithQuery();
    }


}
