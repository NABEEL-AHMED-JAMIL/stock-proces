package com.stock.process.repository;

import com.stock.process.model.StockVector;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Nabeel Ahmed
 */
@Repository
public interface StockVectorRepository extends CrudRepository<StockVector, Long> {
}
