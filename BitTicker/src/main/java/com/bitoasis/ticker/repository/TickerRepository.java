package com.bitoasis.ticker.repository;

import com.bitoasis.ticker.entity.Ticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TickerRepository extends JpaRepository<Ticker,Long> {

    @Modifying
    @Query("delete from Ticker t")
    void deleteAllWithQuery();
}
