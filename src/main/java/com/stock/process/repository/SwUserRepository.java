package com.stock.process.repository;

import com.stock.process.model.SwUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Nabeel Ahmed
 */
@Repository
public interface SwUserRepository extends CrudRepository<SwUser, Long> {
}
