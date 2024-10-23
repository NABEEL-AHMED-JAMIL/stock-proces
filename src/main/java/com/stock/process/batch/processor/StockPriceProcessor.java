package com.stock.process.batch.processor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.stock.process.efs.EfsFileExchange;
import com.stock.process.enums.FileStatus;
import com.stock.process.model.AuditLog;
import com.stock.process.model.FileInfo;
import com.stock.process.repository.AuditLogRepository;
import com.stock.process.repository.FileInfoRepository;
import com.stock.process.util.BarcoUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Nabeel Ahmed
 */
public class StockPriceProcessor implements Runnable {

    private Logger logger = LoggerFactory.getLogger(StockPriceProcessor.class);

    private FileInfo fileInfo;
    private final EfsFileExchange efsFileExchange;
    private final FileInfoRepository fileInfoRepository;
    private final AuditLogRepository auditLogRepository;

    public StockPriceProcessor(EfsFileExchange efsFileExchange,
        FileInfoRepository fileInfoRepository,
        AuditLogRepository auditLogRepository) {
        this.efsFileExchange = efsFileExchange;
        this.fileInfoRepository = fileInfoRepository;
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        logger.info("StockPriceProcessor :: run :: start time {} ms", startTime);
        try {
            // Queue to Running
            this.fileInfo.setFileStatus(FileStatus.Running);
            this.fileInfoRepository.save(this.fileInfo);
            this.auditLogMessage("Process change for [%s] [Queue => Running] status.");
            // check the file type and read the file and after that convert into json file format
            if (BarcoUtil.isNull(this.fileInfo.getPath()) || !this.efsFileExchange.doesFileExist(this.fileInfo.getPath())) {
                this.fileInfo.setFileStatus(FileStatus.Failed);
                this.fileInfoRepository.save(this.fileInfo);
                this.auditLogMessage("Process change for [%s] [Running => Failed] status due to file not exist or either path not correct.");
                return;
            }
            if (this.fileInfo.getType().equals("CSV")) {
                this.readCSVFile();
            } else if (this.fileInfo.getType().equals("PARQUET")) {
                this.readPARQUETFile();
            }
            // Running to Completed
            this.fileInfo.setFileStatus(FileStatus.Completed);
            this.fileInfoRepository.save(this.fileInfo);
            this.auditLogMessage("Process change for [%s] [Running => Completed] status.");
        } catch (Exception ex) {
            logger.error("Error occurred while processing file {}: {}", this.fileInfo.getId(), ex.getMessage());
            this.fileInfo.setFileStatus(FileStatus.Failed);
            this.fileInfoRepository.save(this.fileInfo);
            this.auditLogMessage("Process change for [%s] [Running => Failed] status due to error.");
        } finally {
            long endTime = System.currentTimeMillis();
            logger.info("StockPriceProcessor :: run :: end time {} ms", endTime);
        }
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    /**
     * Message use to add into audit
     * @param message
     * */
    private void auditLogMessage(String message) {
        AuditLog auditLog = new AuditLog();
        auditLog.setLogsDetail(String.format(message, this.fileInfo.getFilename()));
        auditLog.setFileInfo(this.fileInfo);
        this.auditLogRepository.save(auditLog);
    }

    /**
     * Method use to read the csv file and process
     * */
    private void readCSVFile() throws Exception {
        File file = this.efsFileExchange.getFile(this.fileInfo.getPath());
        JsonObject jsonObject = getJsonObject(FileUtils.readLines(file, "UTF-8"));
        // Convert JsonObject to JSON string using Gson
        String segmentPath = this.fileInfo.getFolder().concat("/").concat(this.fileInfo.getFilename().replace(".csv",".json"));
        this.fileInfo.setSegmentPath(segmentPath);
        Gson gson = new Gson();
        String jsonString = gson.toJson(jsonObject);
        // Create a ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // Write the JSON string into the ByteArrayOutputStream as bytes
        byteArrayOutputStream.write(jsonString.getBytes(StandardCharsets.UTF_8));
        this.efsFileExchange.saveFile(byteArrayOutputStream, this.fileInfo.getSegmentPath());
        this.fileInfoRepository.save(this.fileInfo); // the path for segment path
        String message = String.format("File creating for [%s]", segmentPath);
        this.auditLogMessage("Process action for [%s], "+message);
    }

    /**
     * Method use to read the parquet file and process
     * */
    private void readPARQUETFile() {

    }

    /**
     * Method use to generate the json object
     * @param lines
     * @return JsonObject
     * */
    private static JsonObject getJsonObject(List<String> lines) {
        boolean isHeader = true;
        int rowCount = 0;
        JsonObject jsonObject = new JsonObject();
        JsonObject dataView = new JsonObject();
        JsonArray rows = new JsonArray();
        for (String line : lines) {
            String[] values = line.split(","); // Assuming the CSV is comma-separated
            if (isHeader) {
                dataView.addProperty("totalColumns", values.length);
                JsonArray columns = new JsonArray();
                for (int i = 0; i < values.length; i++) {
                    JsonObject column = new JsonObject();
                    column.addProperty("name", values[i]);
                    column.addProperty("order", i);
                    columns.add(column);
                }
                dataView.add("columns", columns);
                isHeader = false;
            } else {
                rowCount++;
                JsonObject text = new JsonObject();
                JsonArray textArray = new JsonArray();
                for (String value : values) {
                    textArray.add(value);
                }
                text.add("text", textArray);
                rows.add(text);
            }
        }
        dataView.addProperty("totalRows", rowCount);
        dataView.add("rows", rows); // add rows
        jsonObject.add("dataView", dataView);
        return jsonObject;
    }

}
