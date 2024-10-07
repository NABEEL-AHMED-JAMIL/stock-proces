package com.stock.process.repository;

import com.stock.process.model.StockPrice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Nabeel Ahmed
 */
@Repository
public interface StockPriceRepository extends CrudRepository<StockPrice, Long> {
}
