package com.stock.process.repository;

import com.stock.process.model.SwTaskFile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Nabeel Ahmed
 */
@Repository
public interface SwTaskFileRepository extends CrudRepository<SwTaskFile, Long> {
}
