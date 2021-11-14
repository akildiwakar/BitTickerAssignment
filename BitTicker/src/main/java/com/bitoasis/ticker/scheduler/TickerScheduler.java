package com.bitoasis.ticker.scheduler;

import com.bitoasis.ticker.service.TickerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;

@Configuration
@EnableScheduling
@Log4j2
public class TickerScheduler {
    @Autowired
    private TickerService tickerService;

    /**
     * Trigger to fetch Data and persist it into the Database after every fixed delay
     */
    @Scheduled(fixedDelayString = "${scheduler.delay}")
    public void tickerTrigger() {
        log.info("Scheduled Trigger Invoked...");
        tickerService.scheduledTrigger();
    }
}
