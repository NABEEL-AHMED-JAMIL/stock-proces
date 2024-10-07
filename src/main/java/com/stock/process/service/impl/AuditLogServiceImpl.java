package com.stock.process.service.impl;

import com.stock.process.repository.AuditLogRepository;
import com.stock.process.service.AuditLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Nabeel Ahmed
 */
@Service
public class AuditLogServiceImpl implements AuditLogService {

    private Logger logger = LoggerFactory.getLogger(AuditLogServiceImpl.class);

    @Autowired
    private AuditLogRepository auditLogRepository;

    public AuditLogServiceImpl() {}

}
