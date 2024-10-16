package com.stock.process.api;

import com.stock.process.dto.AppResponse;
import com.stock.process.dto.FileInfoDto;
import com.stock.process.dto.FileUploadRequest;
import com.stock.process.service.FileInfoService;
import com.stock.process.util.BarcoUtil;
import com.stock.process.util.ExceptionUtil;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Api use to perform crud operation
 * @author Nabeel Ahmed
 */
@Controller
public class BatchProcessRestApi {

    private Logger logger = LoggerFactory.getLogger(BatchProcessRestApi.class);

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(BarcoUtil.SIMPLE_DATE_PATTERN);

    @Autowired
    private FileInfoService fileInfoService;

    public BatchProcessRestApi() {}

    /**
     * @apiName :- index
     * api use to load the main page
     * @return ResponseEntity<?>
     * */
    @GetMapping(value="/index")
    public String indexPage(Model model) throws Exception {
        logger.info("BatchProcessRestApi :: index -> call");
        model.addAttribute("date", LocalDate.now().format(formatter));
        model.addAttribute("pageNumber", 1);
        model.addAttribute("pageSize", 100);
        model.addAttribute("appResponse", this.fileInfoService.fetchFileCountByMonth(BarcoUtil.currentMonth()));
        return "index";
    }

    /**
     * @apiName :- upload-file
     * api use to load the upload page
     * @return ResponseEntity<?>
     * */
    @GetMapping(value="/upload-file")
    public String uploadFilePage(Model model) throws Exception {
        logger.info("BatchProcessRestApi :: uploadFilePage -> call");
        model.addAttribute("date", LocalDate.now().format(formatter));
        model.addAttribute("pageNumber", 1);
        model.addAttribute("pageSize", 100);
        return "upload-file";
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
    public String fetchFileListByDate(@RequestParam String date, @RequestParam(defaultValue = "1") Integer pageNumber,
        @RequestParam(defaultValue = "100") Integer pageSize, Model model) {
        logger.info("BatchProcessRestApi :: fetchFileListByDate -> call date={} pageNumber={} pageSize={}", date, pageNumber, pageSize);
        try {
            Page<FileInfoDto> fileInfos = this.fileInfoService.fetchFileListByDate(date, pageNumber, pageSize);
            int totalPages = fileInfos.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
                model.addAttribute("pageSize", pageSize);
            }
            // set the current date
            model.addAttribute("currentDate", date);
            // set the response from the server
            model.addAttribute("appResponse", new AppResponse(BarcoUtil.SUCCESS, "Data Fetch Successfully.", fileInfos));
        } catch (Exception ex) {
            logger.error("An error occurred while fetchFileListByDate ", ExceptionUtil.getRootCause(ex));
            model.addAttribute("appResponse", new AppResponse(BarcoUtil.ERROR, ExceptionUtil.getRootCauseMessage(ex)));
        }
        return "file-list";
    }

    /**
     * @apiName :- uploadFile
     * @apiNote :- Method use to upload file
     * @param payload
     * @return ResponseEntity
     * */
    @PostMapping(value="/uploadFile")
    public String uploadFile(Model model, FileUploadRequest payload) {
        logger.info("BatchProcessRestApi :: uploadFile -> call ");
        try {
            model.addAttribute("date", LocalDate.now().format(formatter));
            model.addAttribute("pageNumber", 1);
            model.addAttribute("pageSize", 100);
            if (!BarcoUtil.isNull(payload.getFile())) {
                model.addAttribute("appResponse", this.fileInfoService.uploadFile(payload, false));
            } else {
                model.addAttribute("appResponse", new AppResponse(BarcoUtil.ERROR, "File not found."));
            }
        } catch (Exception ex) {
            logger.error("An error occurred while uploadFile ", ExceptionUtil.getRootCause(ex));
            model.addAttribute("appResponse", new AppResponse(BarcoUtil.ERROR, ExceptionUtil.getRootCauseMessage(ex)));
        }
        return "upload-file";
    }

    /**
     * @apiName :- fetchFileAuditLog
     * @apiNote :- Api use to fetch file audit logs
     * @param fileId
     * @return ResponseEntity<?>
     * */
    @GetMapping(value="/fetchFileAuditLog")
    public String fetchFileAuditLog(Model model, Integer fileId) {
        logger.info("BatchProcessRestApi :: fetchFileAuditLog -> call fileId={} ", fileId);
        try {
            // always go back to first page
            model.addAttribute("date", LocalDate.now().format(formatter));
            model.addAttribute("pageNumber", 1);
            model.addAttribute("pageSize", 100);
            model.addAttribute("appResponse", this.fileInfoService.fetchFileAuditLog(fileId));
        } catch (Exception ex) {
            logger.error("An error occurred while fetchFileAuditLog ", ExceptionUtil.getRootCause(ex));
            model.addAttribute("appResponse", new AppResponse(BarcoUtil.ERROR, ExceptionUtil.getRootCauseMessage(ex)));
        }
        return "audit-log";
    }

    /**
     * @apiName :- deleteFileById
     * @apiNote :- Api use to delete file by id
     * @param fileId
     * @return ResponseEntity<?>
     * */
    @GetMapping(value="/deleteFileById")
    public String deleteFileById(@RequestParam Long fileId) {
        logger.info("BatchProcessRestApi :: deleteFileById -> call fileId={} ", fileId);
        String currentDate = LocalDate.now().format(formatter);
        int pageNumber = 1;
        int pageSize = 100;
        try {
            this.fileInfoService.deleteFileById(fileId);
        } catch (Exception ex) {
            logger.error("An error occurred while deleting the file: {}", ExceptionUtil.getRootCauseMessage(ex));
        }
        return String.format("redirect:/fetchFileListByDate?date=%s&pageNumber=%d&pageSize=%d", currentDate, pageNumber, pageSize);
    }

    /**
     * @apiName :- deleteFileById
     * @apiNote :- Api use to delete file by id
     * @param fileId
     * @return ResponseEntity<?>
     * */
    @GetMapping(value="/runFileById")
    public String runFileById(@RequestParam Long fileId) {
        logger.info("BatchProcessRestApi :: runFileById -> call fileId={} ", fileId);
        String currentDate = LocalDate.now().format(formatter);
        int pageNumber = 1;
        int pageSize = 100;
        try {
            this.fileInfoService.runFileById(fileId);
        } catch (Exception ex) {
            logger.error("An error occurred while run the file: {} ", ExceptionUtil.getRootCauseMessage(ex));
        }
        return String.format("redirect:/fetchFileListByDate?date=%s&pageNumber=%d&pageSize=%d", currentDate, pageNumber, pageSize);
    }

}
