package com.stock.process.api;

import com.stock.process.domain.dto.AppResponse;
import com.stock.process.domain.dto.FileInfoDto;
import com.stock.process.domain.enums.FileStatus;
import com.stock.process.domain.service.FileInfoService;
import com.stock.process.util.BarcoUtil;
import com.stock.process.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Api use to perform crud operation
 * @author Nabeel Ahmed
 */
@RestController
@CrossOrigin(origins="*")
@RequestMapping(value="/stock-dashboard.json")
public class StockDashboardRestApi {

    private Logger logger = LoggerFactory.getLogger(StockDashboardRestApi.class);

    private final FileInfoService fileInfoService;

    public StockDashboardRestApi(FileInfoService fileInfoService) {
        this.fileInfoService = fileInfoService;
    }

    /**
     * @apiName :- fetchFileListByDateAndFileStatus
     * @apiNote :- Api use to fetch file list by date and page
     * @param date
     * @param pageSize
     * @param pageNumber
     * @return ResponseEntity<?>
     * */
    @GetMapping(value="/fetchFileListByDateAndFileStatus")
    public ResponseEntity<?> fetchFileListByDateAndFileStatus(@RequestParam String date, @RequestParam FileStatus fileStatus,
        @RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "100") Integer pageSize) {
        logger.info("StockDashboardRestApi :: fetchFileListByDateAndFileStatus -> call date={} fileStatus={} pageNumber={} pageSize={}", date, fileStatus, pageNumber, pageSize);
        try {
            Page<FileInfoDto> fileInfos = this.fileInfoService.fetchFileListByDateAndFileStatus(date, fileStatus, pageNumber, pageSize);
            return new ResponseEntity<>(new AppResponse(BarcoUtil.SUCCESS, "Data Fetch Successfully.", fileInfos), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while fetchFileListByDateAndFileStatus ", ExceptionUtil.getRootCause(ex));
            return new ResponseEntity<>(new AppResponse(BarcoUtil.ERROR, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @apiName :- fetchProcessFileByStatus
     * @apiNote :- Api use to fetch file list by date and page
     * @param fileId
     * @return ResponseEntity<?>
     * */
    @GetMapping(value="/fetchProcessFileByStatus")
    public ResponseEntity<?> fetchProcessFileByStatus(@RequestParam Integer fileId) {
        logger.info("StockDashboardRestApi :: fetchProcessFileByStatus -> call fileId={}.", fileId);
        try {
            return new ResponseEntity<>(this.fileInfoService.fetchProcessFileByStatus(fileId), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while fetchProcessFileByStatus ", ExceptionUtil.getRootCause(ex));
            return new ResponseEntity<>(new AppResponse(BarcoUtil.ERROR, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @apiName :- fetchAllActive
     * @apiNote :- Api to fetch all active files
     * @return ResponseEntity<?>
     * */
    @GetMapping(value="/fetchAllActiveAskQuestions")
    public ResponseEntity<?> fetchAllActiveAskQuestions() {
        logger.info("StockDashboardRestApi :: fetchAllActiveAskQuestions.");
        try {
            return new ResponseEntity<>(this.fileInfoService.fetchAllActiveAskQuestion(), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while fetchAllActiveAskQuestion ", ExceptionUtil.getRootCause(ex));
            return new ResponseEntity<>(new AppResponse(BarcoUtil.ERROR, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

}
