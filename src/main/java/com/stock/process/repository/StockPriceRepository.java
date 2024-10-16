package com.stock.process.repository;

import com.stock.process.enums.Status;
import com.stock.process.model.FileInfo;
import com.stock.process.model.StockPrice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

/**
 * @author Nabeel Ahmed
 */
@Repository
public interface StockPriceRepository extends CrudRepository<StockPrice, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE StockPrice s SET s.status = :status WHERE s.fileInfo = :fileInfo")
    public int updateStatusByFileId(@Param("status") Status status, @Param("fileInfo") FileInfo fileInfo);
}
