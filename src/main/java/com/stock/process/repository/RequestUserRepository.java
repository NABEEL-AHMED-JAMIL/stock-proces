package com.stock.process.repository;

import com.stock.process.model.RequestUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Nabeel Ahmed
 */
@Repository
public interface RequestUserRepository extends CrudRepository<RequestUser, Long> {
}
