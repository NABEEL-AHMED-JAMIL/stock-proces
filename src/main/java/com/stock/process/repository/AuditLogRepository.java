package com.stock.process.repository;

import com.stock.process.model.AuditLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Nabeel Ahmed
 */
@Repository
public interface AuditLogRepository extends CrudRepository<AuditLog, Long> {
}
