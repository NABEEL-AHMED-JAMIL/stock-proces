package com.stock.process.repository;

import org.apache.tomcat.jni.FileInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Nabeel Ahmed
 */
@Repository
public interface SwTaskAuditLogRepository extends CrudRepository<FileInfo, Long> {
}
