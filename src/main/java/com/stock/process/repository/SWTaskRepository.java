package com.stock.process.repository;

import com.stock.process.model.SWTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Nabeel Ahmed
 */
@Repository
public interface SWTaskRepository extends CrudRepository<SWTask, Long> {
}
