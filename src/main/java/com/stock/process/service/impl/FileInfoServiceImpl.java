package com.stock.process.service.impl;

import com.stock.process.dto.AppResponse;
import com.stock.process.dto.FileUploadRequest;
import com.stock.process.efs.EfsFileExchange;
import com.stock.process.repository.AuditLogRepository;
import com.stock.process.repository.FileInfoRepository;
import com.stock.process.service.FileInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Nabeel Ahmed
 */
@Service
public class FileInfoServiceImpl implements FileInfoService {

    private Logger logger = LoggerFactory.getLogger(FileInfoServiceImpl.class);

    @Autowired
    private FileInfoRepository fileInfoRepository;
    @Autowired
    private AuditLogRepository auditLogRepository;
    @Autowired
    private EfsFileExchange efsFileExchange;

    public FileInfoServiceImpl() {}

    /**
     * Method use to fetch file list by date
     * @param date
     * @param pageSize
     * @param pageNumber
     * @return AppResponse
     * @throws Exception
     * */
    @Override
    public AppResponse fetchFileListByDate(String date, Integer pageNumber, Integer pageSize) throws Exception {
        logger.info("FileInfoServiceImpl :: fetchFileListByDate -> call date=%s pageNumber=%d pageSize=%d", date, pageNumber, pageSize);
        // check is valid date
        // check is valid page pageNumber
        // check is valid page size
        return null;
    }

    /**
     * Method use to upload file
     * @param payload
     * @return AppResponse
     * @throws Exception
     * */
    @Override
    public AppResponse uploadFile(FileUploadRequest payload) throws Exception {
        return null;
    }

    /**
     * Method use to download file by id
     * @param fileId
     * @return ByteArrayOutputStream
     * @throws Exception
     * */
    @Override
    public AppResponse downloadFileById(Long fileId) throws Exception {
        return null;
    }

    /**
     * Method use to delete file by id
     * @param fileId
     * @return AppResponse
     * @throws Exception
     * */
    @Override
    public AppResponse deleteFileById(Long fileId) throws Exception {
        return null;
    }
}
