package com.stock.process.api;

import com.stock.process.dto.AppResponse;
import com.stock.process.dto.FileUploadRequest;
import com.stock.process.service.AuditLogService;
import com.stock.process.service.FileInfoService;
import com.stock.process.service.StockPriceService;
import com.stock.process.util.BarcoUtil;
import com.stock.process.util.ExceptionUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Api use to perform crud operation
 * @author Nabeel Ahmed
 */
@Controller
public class BatchProcessRestApi {

    private Logger logger = LoggerFactory.getLogger(BatchProcessRestApi.class);

    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private FileInfoService fileInfoService;
    @Autowired
    private StockPriceService stockPriceService;

    public BatchProcessRestApi() {}

    /**
     * @apiName :- uploadFile
     * @apiNote :- Method use to upload file
     * @param payload
     * @return ResponseEntity
     * */
    @ApiOperation(value = "Api use to upload file.", response = ResponseEntity.class)
    @RequestMapping(value="/uploadFile", method= RequestMethod.POST)
    public ResponseEntity<?> uploadFile(FileUploadRequest payload) {
        try {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("An error occurred while uploadFile ", ExceptionUtil.getRootCause(ex));
            return new ResponseEntity<>(new AppResponse(BarcoUtil.ERROR, ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // view upload file by date wise & pagination

}
