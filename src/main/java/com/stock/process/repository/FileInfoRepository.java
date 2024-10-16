package com.stock.process.repository;

import com.stock.process.dto.StatisticDto;
import com.stock.process.enums.FileStatus;
import com.stock.process.enums.Status;
import com.stock.process.model.FileInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.util.List;

/**
 * @author Nabeel Ahmed
 */
@Repository
public interface FileInfoRepository extends CrudRepository<FileInfo, Long> {

    @Query("SELECT f FROM FileInfo f WHERE DATE(f.dateCreated) = :dateCreated")
    public Page<FileInfo> findAllByDateCreated(@Param("dateCreated") Date dateCreated, Pageable pageable);

    @Query(value = "SELECT TO_CHAR(date_created, 'YYYY-MM-DD') AS date, COUNT(*) AS totalCount " +
        "FROM file_info " +
        "WHERE TO_CHAR(date_created, 'YYYY-MM') = :month AND status = 1 " +
        "GROUP BY TO_CHAR(date_created, 'YYYY-MM-DD')",
        nativeQuery = true)
    public List<StatisticDto> findFileInfoCountsByMonth(@Param("month") String month);

    @Query("SELECT TO_CHAR(f.dateCreated, 'YYYY-MM-DD') AS date, f.fileStatus AS fileStatus, COUNT(f) AS totalCount " +
        "FROM FileInfo f " +
        "WHERE TO_CHAR(f.dateCreated, 'YYYY-MM') = :month AND status = 1  " +
        "GROUP BY TO_CHAR(f.dateCreated, 'YYYY-MM-DD'), f.fileStatus")
    public List<StatisticDto> findFileCountGroupedByDateAndStatus(@Param("month") String month);

    @Query("SELECT f FROM FileInfo f WHERE f.status = :status AND f.fileStatus = :fileStatus ORDER BY f.id ASC")
    public List<FileInfo> findTop10ByStatusAndFileStatus(@Param("status") Status status, @Param("fileStatus") FileStatus fileStatus, Pageable pageable);

}
