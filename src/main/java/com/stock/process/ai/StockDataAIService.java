package com.stock.process.ai;

import com.stock.process.domain.model.AuditLog;
import com.stock.process.domain.model.FileInfo;
import com.stock.process.domain.model.StockVector;
import com.stock.process.domain.repository.AuditLogRepository;
import com.stock.process.domain.repository.StockVectorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author Nabeel Ahmed
 */
@Service
public class StockDataAIService {

    private Logger logger = LoggerFactory.getLogger(StockDataAIService.class);

    private final AuditLogRepository auditLogRepository;
    private final StockVectorRepository stockVectorRepository;

    public StockDataAIService(
        AuditLogRepository auditLogRepository,
        StockVectorRepository stockVectorRepository) {
        this.auditLogRepository = auditLogRepository;
        this.stockVectorRepository = stockVectorRepository;
    }

    public void embedStockData(String value, FileInfo fileInfo) {
        try {
            StockVector stockVector = new StockVector();
            stockVector.setContent(value);
            stockVector.setFileInfo(fileInfo);
            this.stockVectorRepository.save(stockVector);
        } catch (Exception ex) {
            logger.error("Error occurred while processing file {}: {}", fileInfo.getId(), value);
            this.auditLogMessage(String.format("Error occurred while processing file [%d::%s]", fileInfo.getId(), value), fileInfo);
        }
    }

    private void auditLogMessage(String message, FileInfo fileInfo) {
        AuditLog auditLog = new AuditLog();
        auditLog.setLogsDetail(String.format(message, fileInfo.getFilename()));
        auditLog.setFileInfo(fileInfo);
        this.auditLogRepository.save(auditLog);
    }
}
