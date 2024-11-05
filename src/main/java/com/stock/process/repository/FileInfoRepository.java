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

    @Query(value = "SELECT f.type AS fileType, COUNT(*) AS totalCount " +
        "FROM file_info f " +
        "WHERE DATE(f.date_created) = CURRENT_DATE AND f.status = 0 " +
        "GROUP BY fileType",
        nativeQuery = true)
    public List<StatisticDto> fetchFileInfoCountsTodayByType();

    @Query(value = "SELECT TO_CHAR(f.date_created, 'YYYY-MM-DD') AS date, COUNT(*) AS totalCount " +
        "FROM file_info f " +
        "WHERE TO_CHAR(f.date_created, 'YYYY-MM') = :month AND f.status = 0 " +
        "GROUP BY date",
        nativeQuery = true)
    public List<StatisticDto> fetchFileInfoCurrentMonthDailyCount(@Param("month") String month);

    @Query(value = "SELECT TO_CHAR(f.date_created, 'YYYY-MM-DD') AS date, f.file_status AS fileStatus, COUNT(f) AS totalCount " +
        "FROM file_info f " +
        "WHERE TO_CHAR(f.date_created, 'YYYY-MM') = :month AND f.status = 0 " +
        "GROUP BY date, f.file_status",
        nativeQuery = true)
    public List<StatisticDto> fetchFileInfoCurrentMonthDailyCountByFileStatus(@Param("month") String month);

    @Query(value = "SELECT TO_CHAR(f.date_created, 'YYYY-MM-DD') AS date, f.type AS fileType, COUNT(*) AS totalCount " +
        "FROM file_info f " +
        "WHERE TO_CHAR(f.date_created, 'YYYY-MM') = :month AND f.status = 0 " +
        "GROUP BY date, fileType " +
        "ORDER BY date ASC",
        nativeQuery = true)
    public List<StatisticDto> fetchFileInfoCurrentMonthDailyCountByType(@Param("month") String month);

    @Query("SELECT f FROM FileInfo f WHERE DATE(f.dateCreated) = :dateCreated AND f.type IN :type ORDER BY f.id DESC")
    public Page<FileInfo> findAllByDateCreatedAndTypeIn(@Param("dateCreated") Date dateCreated, @Param("type") List<String> type, Pageable pageable);

    @Query("SELECT f FROM FileInfo f WHERE DATE(f.dateCreated) = :dateCreated AND f.fileStatus = :fileStatus AND f.type IN :type AND f.segmentPath IS NOT NULL ORDER BY f.id DESC")
    public Page<FileInfo> fetchFileListByDateAndFileStatusAndTypeIn(@Param("dateCreated") Date dateCreated, @Param("fileStatus") FileStatus fileStatus, @Param("type") List<String> type, Pageable pageable);

    @Query("SELECT f FROM FileInfo f WHERE f.status = :status AND f.fileStatus = :fileStatus AND f.type IN :type ORDER BY f.id ASC")
    public List<FileInfo> findTop20ByStatusAndFileStatusAndTypeIn(@Param("status") Status status, @Param("fileStatus") FileStatus fileStatus, @Param("type") List<String> type, Pageable pageable);

}
