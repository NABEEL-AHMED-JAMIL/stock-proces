package com.stock.process.service.impl;

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

    public FileInfoServiceImpl() {}

}
