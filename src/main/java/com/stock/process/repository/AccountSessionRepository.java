package com.stock.process.repository;

import com.stock.process.model.AccountSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Nabeel Ahmed
 */
@Repository
public interface AccountSessionRepository extends CrudRepository<AccountSession, Long> {
}
