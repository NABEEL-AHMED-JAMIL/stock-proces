package com.stock.process.batch;

import com.stock.process.batch.async.executor.AsyncDALTaskExecutor;
import com.stock.process.batch.processor.StockPriceProcessor;
import com.stock.process.efs.EfsFileExchange;
import com.stock.process.enums.FileStatus;
import com.stock.process.enums.Status;
import com.stock.process.model.AuditLog;
import com.stock.process.repository.AuditLogRepository;
import com.stock.process.repository.FileInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Nabeel Ahmed
 */
@Component
public class MyScheduledTask {

    private Logger logger = LoggerFactory.getLogger(MyScheduledTask.class);

    private Integer page = 0;
    private Integer size = 20;

    @Autowired
    private FileInfoRepository fileInfoRepository;
    @Autowired
    private AuditLogRepository auditLogRepository;
    @Autowired
    private EfsFileExchange efsFileExchange;

    public MyScheduledTask() {}

    /**
     * Method use to run every 1 mint
     * */
    @Scheduled(cron = "0 * * * * ?")  // Every minute
    @SchedulerLock(name = "stockFile_queueTask", lockAtLeastFor = "5S", lockAtMostFor = "10M")
    public void queueTask() {
        logger.info("queueTask :: start time [ {} ] ms", System.currentTimeMillis());
        this.fileInfoRepository.findTop10ByStatusAndFileStatus(Status.Active, FileStatus.Pending, PageRequest.of(page, size))
        .forEach(fileInfo -> {
            fileInfo.setFileStatus(FileStatus.Queue);
            this.fileInfoRepository.save(fileInfo);
            // Create and save the audit log
            AuditLog auditLog = new AuditLog();
            auditLog.setLogsDetail(String.format("Process change for [%s] [Pending => Queue] status.", fileInfo.getFilename()));
            auditLog.setFileInfo(fileInfo);
            this.auditLogRepository.save(auditLog);

        });
        logger.info("queueTask :: end time [ {} ] ms", System.currentTimeMillis());
    }

    /**
     * Method use to run every 1 mint
     * */
    @Scheduled(cron = "0 * * * * ?")  // Every minute
    @SchedulerLock(name = "stockFile_runTask", lockAtLeastFor = "5S", lockAtMostFor = "10M")
    public void runTask() {
        logger.info("runTask :: start time [ {} ] ms", System.currentTimeMillis());
        this.fileInfoRepository.findTop10ByStatusAndFileStatus(Status.Active, FileStatus.Queue, PageRequest.of(page, size))
            .forEach(fileInfo -> {
                StockPriceProcessor stockPriceProcessor = new StockPriceProcessor(efsFileExchange, fileInfoRepository, auditLogRepository);
                stockPriceProcessor.setFileInfo(fileInfo);
                AsyncDALTaskExecutor.addTask(stockPriceProcessor);
            });
        logger.info("runTask :: end time [ {} ] ms", System.currentTimeMillis());
    }

}
