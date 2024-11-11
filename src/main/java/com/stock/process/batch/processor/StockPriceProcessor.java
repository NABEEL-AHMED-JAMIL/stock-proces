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
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
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

    /**
     * Thread use to convert the csv and par file to json file
     * */
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
            if (this.fileInfo.getType().equals("CSV") || this.fileInfo.getType().equals("TXT")) {
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
        // Convert JsonObject to JSON string using Gson
        String segmentPath = null;
        if (this.fileInfo.getType().equals("CSV")) {
            segmentPath = this.fileInfo.getFolder().concat("/")
                 .concat(this.fileInfo.getFilename().replace(".csv",".json"));
        } else {
            segmentPath = this.fileInfo.getFolder().concat("/")
                 .concat(this.fileInfo.getFilename().replace(".txt",".json"));
        }
        this.fileInfo.setSegmentPath(segmentPath);
        Gson gson = new Gson();
        File file = this.efsFileExchange.getFile(this.fileInfo.getPath());
        JsonObject jsonObject = getJsonObject(FileUtils.readLines(file, "UTF-8"));
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
    private void readPARQUETFile() throws Exception {
        // working on
    }

    /**
     * Method use to generate the json object
     * @param lines
     * @return JsonObject
     * */
    private JsonObject getJsonObject(List<String> lines) {
        int rowCount = 0;
        boolean isHeader = true;
        // Initialize statistics objects
        DescriptiveStatistics[] statisticsArray = {
            new DescriptiveStatistics(), // Open
            new DescriptiveStatistics(), // High
            new DescriptiveStatistics(), // Low
            new DescriptiveStatistics(), // Close
            new DescriptiveStatistics(), // Volume
            new DescriptiveStatistics()  // OpenInt
        };
        JsonObject jsonObject = new JsonObject();
        JsonObject dataView = new JsonObject();
        JsonArray rows = new JsonArray();
        for (String line : lines) {
            // Assuming the CSV is comma-separated
            String[] values = line.split(",");
            if (isHeader) {
                dataView.addProperty("totalColumns", values.length);
                dataView.add("columns", this.createColumns(values));
                isHeader = false;
            } else {
                rowCount++;
                JsonObject text = new JsonObject();
                text.add("text", this.createRow(values, statisticsArray));
                rows.add(text);
            }
        }
        // Add summary details to dataView
        dataView.addProperty("totalRows", rowCount);
        dataView.add("rows", rows);
        dataView.add("summary", this.createSummary(statisticsArray));
        jsonObject.add("dataView", dataView);
        return jsonObject;
    }

    /**
     * Method use to crete the columns
     * @param headers
     * @return JsonArray
     * */
    private JsonArray createColumns(String[] headers) {
        JsonArray columns = new JsonArray();
        for (int index = 0; index < headers.length; index++) {
            JsonObject column = new JsonObject();
            column.addProperty("name", headers[index]);
            column.addProperty("order", index);
            columns.add(column);
        }
        return columns;
    }

    /**
     * Method use to create the row
     * @param values
     * @param statisticsArray
     * @return JsonArray
     * */
    private JsonArray createRow(String[] values, DescriptiveStatistics[] statisticsArray) {
        JsonArray textArray = new JsonArray();
        for (int i = 0; i < values.length; i++) {
            // Add to respective statistics if index matches expected column position
            // Assuming columns 1-6 are Open, High, Low, Close, Volume, OpenInt
            if (i >= 1 && i <= 6) {
                double value = 0.0;
                try {
                    value = Double.parseDouble(values[i]);
                    statisticsArray[i - 1].addValue(value);
                    textArray.add(value);
                } catch (NumberFormatException ex) {
                    // Log or handle invalid number format
                    statisticsArray[i - 1].addValue(0.0);
                    textArray.add(value);
                    logger.info("createRow :: NumberFormatException {}.", ex.getMessage());
                }
            } else {
                // date for 0 index
                textArray.add(values[i]);
            }
        }
        return textArray;
    }

    /**
     * Method use to create the summary
     * @param statisticsArray
     * @return JsonObject
     * */
    private JsonObject createSummary(DescriptiveStatistics[] statisticsArray) {
        JsonObject summary = new JsonObject();
        String[] keys = {"Open", "High", "Low", "Close", "Volume", "OpenInt"};
        for (int i = 0; i < statisticsArray.length; i++) {
            summary.add(keys[i], this.getSummaryDetail(statisticsArray[i]));
        }
        return summary;
    }

    /**
     * Method to get the summary detail.
     * @param stats
     * @return JsonObject
     */
    private JsonObject getSummaryDetail(DescriptiveStatistics stats) {
        JsonObject summaryDetail = new JsonObject();
        summaryDetail.addProperty("count", stats.getN());
        summaryDetail.addProperty("mean", stats.getMean());
        summaryDetail.addProperty("std", stats.getStandardDeviation());
        summaryDetail.addProperty("min", stats.getMin());
        summaryDetail.addProperty("max", stats.getMax());
        summaryDetail.addProperty("firstQuartile", stats.getPercentile(25));
        summaryDetail.addProperty("median", stats.getPercentile(50));
        summaryDetail.addProperty("thirdQuartile", stats.getPercentile(75));
        return summaryDetail;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

}
