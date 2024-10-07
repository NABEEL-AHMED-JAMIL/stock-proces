package com.stock.process.repository;

import com.stock.process.model.FileInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Nabeel Ahmed
 */
@Repository
public interface FileInfoRepository extends CrudRepository<FileInfo, Long> {

    public Page<FileInfo> findAllByDateCreated(String dateCreated, Pageable pageable);

}
