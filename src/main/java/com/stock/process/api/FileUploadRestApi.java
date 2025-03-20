package com.stock.process.api;

import com.stock.process.dto.AppResponse;
import com.stock.process.dto.FileUploadRequest;
import com.stock.process.service.FileInfoService;
import com.stock.process.util.BarcoUtil;
import com.stock.process.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Api use to perform crud operation
 * @author Nabeel Ahmed
 */
@RestController
@CrossOrigin(origins="*")
@RequestMapping(value="/action.json")
public class FileUploadRestApi {

    private Logger logger = LoggerFactory.getLogger(FileUploadRestApi.class);

    @Autowired
    private FileInfoService fileInfoService;

    public FileUploadRestApi() {}

    /**
     * @apiName :- uploadFile
     * @apiNote :- Api use to upload the files
     * @return ResponseEntity<?> uploadLookupData
     * */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResponseEntity<?> uploadFile(FileUploadRequest payload) {
        try {
            if (!BarcoUtil.isNull(payload.getFiles()) && payload.getFiles().size() > 5) {
                return new ResponseEntity<>(new AppResponse(BarcoUtil.ERROR, "Max 5 file allow"), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(this.fileInfoService.uploadFile(payload, true), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while uploadLookup ", ExceptionUtil.getRootCause(ex));
            return new ResponseEntity<>(new AppResponse(BarcoUtil.ERROR, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @apiName :- downloadFileById
     * @apiNote :- Api use to download file
     * @param fileId
     * @return ResponseEntity<?>
     * */
    @RequestMapping(value = "/downloadFileById", method = RequestMethod.GET)
    public ResponseEntity<?> downloadFileById(@RequestParam Long fileId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            AppResponse downloadFileResponse = this.fileInfoService.downloadFileById(fileId);
            // Extract file details
            Map<String, Object> fileDetail = (Map<String, Object>) downloadFileResponse.getData();
            // Set content disposition header to suggest a filename for downloads
            headers.add(BarcoUtil.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDetail.get("name") + "\"");
            // Return the file content in the response body
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(fileDetail.get("content"));
        } catch (NoSuchElementException e) {
            logger.error("File not found for ID: {}", fileId);
            return new ResponseEntity<>(new AppResponse(BarcoUtil.ERROR, "File not found."), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            logger.error("An error occurred while downloading the file: {}", ExceptionUtil.getRootCauseMessage(ex));
            return new ResponseEntity<>(new AppResponse(BarcoUtil.ERROR, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
