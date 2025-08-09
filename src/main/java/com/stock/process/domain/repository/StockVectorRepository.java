package com.stock.process.domain.repository;

import com.stock.process.domain.model.StockVector;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Nabeel Ahmed
 */
@Repository
public interface StockVectorRepository extends CrudRepository<StockVector, Long> {
}
