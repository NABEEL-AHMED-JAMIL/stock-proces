package com.stock.process.service;

import com.stock.process.dto.AppResponse;
import com.stock.process.dto.FileUploadRequest;

/**
 * @author Nabeel Ahmed
 */
public interface FileInfoService {

    public AppResponse fetchFileListByDate(String date, Integer pageNumber, Integer pageSize) throws Exception;

    public AppResponse uploadFile(FileUploadRequest payload) throws Exception;

    public AppResponse downloadFileById(Long fileId) throws Exception;

    public AppResponse deleteFileById(Long fileId) throws Exception;

}
