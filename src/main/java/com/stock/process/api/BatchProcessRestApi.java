package com.stock.process.api;

import com.stock.process.dto.AppResponse;
import com.stock.process.dto.FileInfoDto;
import com.stock.process.service.FileInfoService;
import com.stock.process.util.BarcoUtil;
import com.stock.process.util.ExceptionUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.format.DateTimeFormatter;

/**
 * Api use to perform crud operation
 * @author Nabeel Ahmed
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/batch-process.json")
public class BatchProcessRestApi {

    private Logger logger = LoggerFactory.getLogger(BatchProcessRestApi.class);

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(BarcoUtil.SIMPLE_DATE_PATTERN);

    @Autowired
    private FileInfoService fileInfoService;

    public BatchProcessRestApi() {}

    /**
     * @apiName :- index
     * api use to load the main page
     * @return ResponseEntity<?>
     * */
    @GetMapping(value="/fetchFileCount")
    public ResponseEntity<?> fetchFileCount() {
        logger.info("BatchProcessRestApi :: fetchFileCount -> call");
        try {
            return new ResponseEntity<>(this.fileInfoService.fetchFileCount(BarcoUtil.currentMonth()), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while fetchFileCount ", ExceptionUtil.getRootCause(ex));
            return new ResponseEntity<>(new AppResponse(BarcoUtil.ERROR, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @apiName :- fetchFileListByDate
     * @apiNote :- Api use to fetch file list by date and page
     * @param date
     * @param pageSize
     * @param pageNumber
     * @return ResponseEntity<?>
     * */
    @GetMapping(value="/fetchFileListByDate")
    public ResponseEntity<?> fetchFileListByDate(@RequestParam String date, @RequestParam(defaultValue = "1") Integer pageNumber,
        @RequestParam(defaultValue = "100") Integer pageSize) {
        logger.info("BatchProcessRestApi :: fetchFileListByDate -> call date={} pageNumber={} pageSize={}", date, pageNumber, pageSize);
        try {
            Page<FileInfoDto> fileInfos = this.fileInfoService.fetchFileListByDate(date, pageNumber, pageSize);
            return new ResponseEntity<>(new AppResponse(BarcoUtil.SUCCESS, "Data Fetch Successfully.", fileInfos), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while fetchFileListByDate ", ExceptionUtil.getRootCause(ex));
            return new ResponseEntity<>(new AppResponse(BarcoUtil.ERROR, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @apiName :- fetchFileAuditLog
     * @apiNote :- Api use to fetch file audit logs
     * @param fileId
     * @return ResponseEntity<?>
     * */
    @GetMapping(value="/fetchFileAuditLog")
    public ResponseEntity<?> fetchFileAuditLog(@RequestParam Integer fileId) {
        logger.info("BatchProcessRestApi :: fetchFileAuditLog -> call fileId={} ", fileId);
        try {
            return new ResponseEntity<>(this.fileInfoService.fetchFileAuditLog(fileId), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while fetchFileAuditLog ", ExceptionUtil.getRootCause(ex));
            return new ResponseEntity<>(new AppResponse(BarcoUtil.ERROR, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @apiName :- deleteFileById
     * @apiNote :- Api use to delete file by id
     * @param fileId
     * @return ResponseEntity<?>
     * */
    @DeleteMapping(value="/deleteFileById")
    public ResponseEntity<?> deleteFileById(@RequestParam Long fileId) {
        logger.info("BatchProcessRestApi :: deleteFileById -> call fileId={} ", fileId);
        try {
            this.fileInfoService.deleteFileById(fileId);
            return new ResponseEntity<>(new AppResponse(BarcoUtil.SUCCESS, "File Delete Successfully."), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while deleteFileById ", ExceptionUtil.getRootCause(ex));
            return new ResponseEntity<>(new AppResponse(BarcoUtil.ERROR, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @apiName :- runFileById
     * @apiNote :- Api use to run file by id
     * @param fileId
     * @return ResponseEntity<?>
     * */
    @GetMapping(value="/runFileById")
    public ResponseEntity<?> runFileById(@RequestParam Long fileId) {
        logger.info("BatchProcessRestApi :: runFileById -> call fileId={} ", fileId);
        try {
            this.fileInfoService.runFileById(fileId);
            return new ResponseEntity<>(new AppResponse(BarcoUtil.SUCCESS, "File running start now."), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while fetchFileAuditLog ", ExceptionUtil.getRootCause(ex));
            return new ResponseEntity<>(new AppResponse(BarcoUtil.ERROR, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
