package com.stock.process.repository;

import com.stock.process.model.SwTaskAuditLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Nabeel Ahmed
 */
@Repository
public interface SwTaskAuditLogRepository extends CrudRepository<SwTaskAuditLog, Long> {
}
