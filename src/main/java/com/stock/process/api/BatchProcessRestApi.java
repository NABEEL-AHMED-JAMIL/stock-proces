package com.stock.process.api;

import com.stock.process.dto.AppResponse;
import com.stock.process.dto.FileUploadRequest;
import com.stock.process.service.AuditLogService;
import com.stock.process.service.FileInfoService;
import com.stock.process.service.StockPriceService;
import com.stock.process.util.BarcoUtil;
import com.stock.process.util.ExceptionUtil;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

/**
 * Api use to perform crud operation
 * @author Nabeel Ahmed
 */
@Controller
public class BatchProcessRestApi {

    private Logger logger = LoggerFactory.getLogger(BatchProcessRestApi.class);

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private FileInfoService fileInfoService;
    @Autowired
    private StockPriceService stockPriceService;

    public BatchProcessRestApi() {}

    /**
     * @apiName :- index
     * api use to load the main page
     * @return ResponseEntity<?>
     * */
    @GetMapping(value="/index")
    public String index(Model model) throws Exception {
        logger.info("BatchProcessRestApi :: index -> call");
        model.addAttribute("date", LocalDate.now().format(formatter));
        model.addAttribute("pageNumber", 1);
        model.addAttribute("pageSize", 1000);
        return "index";
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
    public String fetchFileListByDate(@RequestParam String date, @RequestParam Integer pageNumber,
        @RequestParam Integer pageSize, Model model) {
        logger.info("BatchProcessRestApi :: fetchFileListByDate -> call date=%s pageNumber=%d pageSize=%d", date, pageNumber, pageSize);
        try {
            model.addAttribute("appResponse", this.fileInfoService.fetchFileListByDate(date, pageNumber, pageSize));
        } catch (Exception ex) {
            logger.error("An error occurred while fetchFileListByDate ", ExceptionUtil.getRootCause(ex));
            model.addAttribute("appResponse", new AppResponse(BarcoUtil.ERROR, ExceptionUtil.getRootCause(ex)));
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
        try {
            AppResponse appResponse = this.fileInfoService.uploadFile(payload);
            return "upload-file";
        } catch (Exception ex) {
            logger.error("An error occurred while uploadFile ", ExceptionUtil.getRootCause(ex));
            return "upload-file";
        }
    }

    /**
     * @apiName :- downloadFileById
     * @apiNote :- Api use to download file
     * @param fileId
     * @return ResponseEntity<?>
     * */
    @PostMapping(value="/downloadFileById")
    public ResponseEntity<?> downloadFileById(@RequestParam Long fileId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            AppResponse downloadFileResponse = this.fileInfoService.downloadFileById(fileId);
            Map<String, Objects> fileDetail = (Map<String, Objects>) downloadFileResponse.getData();
            headers.add(BarcoUtil.CONTENT_DISPOSITION, BarcoUtil.FILE_NAME_HEADER + fileDetail.get("name"));
            return ResponseEntity.ok().headers(headers).body(fileDetail.get("content"));
        } catch (Exception ex) {
            logger.error("An error occurred while downloadFileById file :- {}.", ExceptionUtil.getRootCauseMessage(ex));
            return new ResponseEntity<>(new AppResponse(BarcoUtil.ERROR, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @apiName :- deleteFileById
     * @apiNote :- Api use to delete file by id
     * @param fileId
     * @return ResponseEntity<?>
     * */
    @RequestMapping(value="/deleteFileById", method=RequestMethod.POST)
    public String deleteFileById(@RequestParam Long fileId, RedirectAttributes redirectAttributes) {
        try {
            AppResponse appResponse = this.fileInfoService.deleteFileById(fileId);
            redirectAttributes.addFlashAttribute("message", "File Deleted Successfully.");
        } catch (Exception ex) {
            logger.error("An error occurred while deleteFileById file :- {}.", ExceptionUtil.getRootCauseMessage(ex));
            redirectAttributes.addFlashAttribute("message", ExceptionUtil.getRootCauseMessage(ex));
        }
        return "redirect:/index";
    }

}
