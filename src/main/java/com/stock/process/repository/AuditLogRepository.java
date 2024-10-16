package com.stock.process.repository;

import com.stock.process.enums.Status;
import com.stock.process.model.AuditLog;
import com.stock.process.model.FileInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;
import org.springframework.data.repository.query.Param;

/**
 * @author Nabeel Ahmed
 */
@Repository
public interface AuditLogRepository extends CrudRepository<AuditLog, Long> {

    public List<AuditLog> findAllByFileInfo(FileInfo fileInfo);

    @Modifying
    @Transactional
    @Query("UPDATE AuditLog a SET a.status = :status WHERE a.fileInfo = :fileInfo")
    public int updateStatusByFileId(@Param("status") Status status, @Param("fileInfo") FileInfo fileInfo);

}
