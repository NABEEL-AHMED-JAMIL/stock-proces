package com.stock.process.service;

import com.stock.process.dto.AppResponse;
import com.stock.process.dto.FileInfoDto;
import com.stock.process.dto.FileUploadRequest;
import org.springframework.data.domain.Page;

/**
 * @author Nabeel Ahmed
 */
public interface FileInfoService {

    public AppResponse fetchFileCountByMonth(String month) throws Exception;

    public Page<FileInfoDto> fetchFileListByDate(String date, Integer pageNumber, Integer pageSize) throws Exception;

    public AppResponse fetchFileAuditLog(Integer fileId) throws Exception;

    public AppResponse uploadFile(FileUploadRequest payload, boolean isMultiple) throws Exception;

    public AppResponse downloadFileById(Long fileId) throws Exception;

    public void deleteFileById(Long fileId) throws Exception;

    public void runFileById(Long fileId) throws Exception;

}
