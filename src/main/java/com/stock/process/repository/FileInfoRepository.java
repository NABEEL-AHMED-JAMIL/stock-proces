package com.stock.process.repository;

import com.stock.process.model.FileInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Nabeel Ahmed
 */
@Repository
public interface FileInfoRepository extends CrudRepository<FileInfo, Long> {
}
