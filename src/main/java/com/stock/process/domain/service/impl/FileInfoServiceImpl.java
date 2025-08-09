package com.stock.process.domain.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.process.domain.dto.*;
import com.stock.process.util.efs.EfsFileExchange;
import com.stock.process.domain.enums.FileStatus;
import com.stock.process.domain.enums.Status;
import com.stock.process.domain.model.AuditLog;
import com.stock.process.domain.model.FileInfo;
import com.stock.process.domain.repository.AskQuestionRepository;
import com.stock.process.domain.repository.AuditLogRepository;
import com.stock.process.domain.repository.FileInfoRepository;
import com.stock.process.domain.service.FileInfoService;
import com.stock.process.util.BarcoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author Nabeel Ahmed
 */
@Service
public class FileInfoServiceImpl implements FileInfoService {

    private Logger logger = LoggerFactory.getLogger(FileInfoServiceImpl.class);

    private final String ROOT_PATH = "C:/stock-price/";
    private final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
        "text/csv",  // MIME type for CSV
        "application/octet-stream", // Generic binary
        "text/plain", // MIME type for TXT
        "application/x-parquet" // MIME type for Parquet (update if needed)
    );
    private final EfsFileExchange efsFileExchange;
    private final FileInfoRepository fileInfoRepository;
    private final AuditLogRepository auditLogRepository;
    private final AskQuestionRepository askQuestionRepository;

    public FileInfoServiceImpl(EfsFileExchange efsFileExchange,
        FileInfoRepository fileInfoRepository,
        AuditLogRepository auditLogRepository,
        AskQuestionRepository askQuestionRepository) {
        this.efsFileExchange = efsFileExchange;
        this.fileInfoRepository = fileInfoRepository;
        this.auditLogRepository = auditLogRepository;
        this.askQuestionRepository = askQuestionRepository;
    }

    /**
     * Method use to fetch the count by month
     * @param month
     * @return AppResponse
     * @throws Exception
     * */
    @Override
    public AppResponse fetchFileCount(String month) throws Exception {
        logger.info("FileInfoServiceImpl :: fetchFileCount -> call month={} ", month);
        Map<String, List<StatisticDto>> statistics = new HashMap<>();
        // daily count by file type like csv = 20, txt = 23, xlsx = 200
        statistics.put("today_count_by_type", this.fileInfoRepository.fetchFileInfoCountsTodayByType());
        // daily count by month
        statistics.put("current_month_daily_count", this.fileInfoRepository.fetchFileInfoCurrentMonthDailyCount(month));
        // daily count by current month and status
        statistics.put("current_month_daily_count_by_file_status", this.fileInfoRepository.fetchFileInfoCurrentMonthDailyCountByFileStatus(month));
        // type mean [csv and date] count and [txt and date] count
        statistics.put("current_month_daily_count_by_type", this.fileInfoRepository.fetchFileInfoCurrentMonthDailyCountByType(month));
        return new AppResponse(BarcoUtil.SUCCESS, "Fetch successfully.", statistics);
    }

    /**
     * Method use to fetch file list by date
     * @param date
     * @param pageSize
     * @param pageNumber
     * @return AppResponse
     * @throws Exception
     * */
    @Override
    public Page<FileInfoDto> fetchFileListByDate(String date, Integer pageNumber, Integer pageSize) throws Exception {
        logger.info("FileInfoServiceImpl :: fetchFileListByDate -> call date={} pageNumber={} pageSize={}", date, pageNumber, pageSize);
        if (BarcoUtil.isBlank(date)) {
            throw new Exception(String.format("Invalid: Data %s.", date));
        } else if (BarcoUtil.isNull(pageNumber) || pageNumber <= 0) {
            throw new Exception(String.format("Invalid: PageNumber %s.", pageNumber));
        } else if (BarcoUtil.isNull(pageSize)) {
            throw new Exception(String.format("Invalid: PageSize %s.", pageSize));
        }
        // if the page is 1 so we check in the zero index so [page number = page number -1]
        pageNumber = pageNumber - 1;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<FileInfo> response = this.fileInfoRepository.findAllByDateCreatedAndTypeIn(Date.valueOf(LocalDate.parse(date)),
            Arrays.asList("CSV", "TXT", "PARQUET"), pageRequest);
        List<FileInfoDto> arrays = new ArrayList<>();
         for(FileInfo fileInfo: response) {
             arrays.add(this.getFileInfoDto(fileInfo));
         }
        return new PageImpl<>(arrays, pageRequest, response.getTotalElements());
    }

    /**
     * Method use to fetch file list by date & status file
     * @param date
     * @param pageSize
     * @param pageNumber
     * @return AppResponse
     * @throws Exception
     * */
    @Override
    public Page<FileInfoDto> fetchFileListByDateAndFileStatus(String date, FileStatus fileStatus, Integer pageNumber, Integer pageSize) throws Exception {
        logger.info("FileInfoServiceImpl :: fetchFileListByDateAndFileStatus -> call date={} fileStatus={} pageNumber={} pageSize={}",
            date, fileStatus, pageNumber, pageSize);
        if (BarcoUtil.isBlank(date)) {
            throw new Exception(String.format("Invalid: Data %s.", date));
        }   else if (BarcoUtil.isNull(fileStatus)) {
            throw new Exception(String.format("Invalid: fileStatus %s.", fileStatus));
        } else if (BarcoUtil.isNull(pageNumber) || pageNumber <= 0) {
            throw new Exception(String.format("Invalid: PageNumber %s.", pageNumber));
        } else if (BarcoUtil.isNull(pageSize)) {
            throw new Exception(String.format("Invalid: PageSize %s.", pageSize));
        }
        // if the page is 1 so we check in the zero index so [page number = page number -1]
        pageNumber = pageNumber - 1;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<FileInfo> response = this.fileInfoRepository.fetchFileListByDateAndFileStatusAndTypeIn(Date.valueOf(LocalDate.parse(date)),
            fileStatus, Arrays.asList("CSV", "TXT", "PARQUET"), pageRequest);
        List<FileInfoDto> arrays = new ArrayList<>();
        for(FileInfo fileInfo: response) {
            arrays.add(this.getFileInfoDto(fileInfo));
        }
        return new PageImpl<>(arrays, pageRequest, response.getTotalElements());
    }

    /**
     * Method use to fetch process file by status
     * @param fileId
     * @return AppResponse
     * @throws Exception
     * */
    @Override
    public AppResponse fetchProcessFileByStatus(Integer fileId) throws Exception {
        logger.info("FileInfoServiceImpl :: fetchProcessFileByStatus -> call fileId={} ", fileId);
        Optional<FileInfo> fileInfo =  this.fileInfoRepository.findById(Long.valueOf(fileId));
        if (!fileInfo.isPresent()) {
            return new AppResponse(BarcoUtil.ERROR, "File info not found with id.");
        } else if (BarcoUtil.isBlank(fileInfo.get().getSegmentPath())) {
            return new AppResponse(BarcoUtil.ERROR, "File info not found with id.");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        // Adjust the path according to your project structure
        File file = this.efsFileExchange.getFile(fileInfo.get().getSegmentPath());
        return new AppResponse(BarcoUtil.SUCCESS, "File fetch successfully.", objectMapper.readValue(file, Object.class));
    }

    /**
     * Method use to fetch file audit logs
     * @param fileId
     * @return AppResponse
     * @throws Exception
     * */
    @Override
    public AppResponse fetchFileAuditLog(Integer fileId) throws Exception {
        logger.info("FileInfoServiceImpl :: fetchFileAuditLog -> call fileId={} ", fileId);
        Optional<FileInfo> fileInfo =  this.fileInfoRepository.findById(Long.valueOf(fileId));
        if (!fileInfo.isPresent()) {
            return new AppResponse(BarcoUtil.ERROR, "File info not found with id.");
        }
        String message = String.format("Audit Log History Detail [%s], [%s] ", fileInfo.get().getFilename(), fileInfo.get().getType());
        return new AppResponse(BarcoUtil.SUCCESS, message, this.auditLogRepository.findAllByFileInfo(
             fileInfo.get()).stream().map(this::getAuditLogDto).collect(Collectors.toList()));
    }

    /**
     * Method use to upload file
     * @param payload
     * @return AppResponse
     * @throws Exception
     * */
    @Override
    public AppResponse uploadFile(FileUploadRequest payload) throws Exception {
        logger.info("FileInfoServiceImpl :: uploadFile -> call");
        // Validate the file type (single or multiple files)
        List<MultipartFile> files = Collections.singletonList(payload.getFile());
        if (!this.isValidFileType(files)) {
            return new AppResponse(BarcoUtil.ERROR, "Invalid file type. Allowed types: CSV, Parquet, TXT.");
        }
        // Create a folder based on today's date
        String folderPath = ROOT_PATH + LocalDate.now();
        // Check if the folder exists or can be created
        if (!this.efsFileExchange.makeDir(folderPath)) {
            return new AppResponse(BarcoUtil.ERROR, String.format("%s not exist", folderPath));
        }
        // Adjust the thread pool size
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<CompletableFuture<Void>> futures = files.stream()
            .map(file -> CompletableFuture.runAsync(() -> this.processSingleFile(folderPath, file), executor))
            .collect(Collectors.toList());
        // Wait for all futures to complete, Blocks until all are done
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        // Clean up the thread pool
        executor.shutdown();
        return new AppResponse(BarcoUtil.SUCCESS, "File(s) saved successfully.");
    }

    /**
     * Method use to fetch all active ask question
     * @return AppResponse
     * @throws Exception
     * */
    @Override
    public AppResponse fetchAllActiveAskQuestion() throws Exception {
        logger.info("FileInfoServiceImpl :: fetchAllActiveAskQuestion -> call");
        return new AppResponse(BarcoUtil.SUCCESS, "Fetch successfully.",
            this.askQuestionRepository.findByIsActiveTrue().stream()
                .map(askQuestion -> {
                    AskQuestionDto askQuestionDto = new AskQuestionDto();
                    askQuestionDto.setId(askQuestion.getId());
                    askQuestionDto.setCategory(askQuestion.getCategory());
                    askQuestionDto.setSourceType(askQuestion.getSourceType());
                    askQuestionDto.setQuestion(askQuestion.getQuestion());
                    return askQuestionDto;
                }).collect(Collectors.toList()));
    }

    /**
     * Method use to download file by id
     * @param fileId
     * @return ByteArrayOutputStream
     * @throws Exception
     * */
    @Override
    public AppResponse downloadFileById(Long fileId) throws Exception {
        logger.info("FileInfoServiceImpl :: downloadFileById -> call fileId={}", fileId);
        Optional<FileInfo> fileInfo = this.fileInfoRepository.findById(fileId);
        // Check if the file exists
        if (!fileInfo.isPresent()) {
            throw new NoSuchElementException("File not found with ID: " + fileId);
        }
        Map<String, Object> fileDetail = new HashMap<>();
        fileDetail.put("name", fileInfo.get().getFilename()); // File name
        // Convert file to byte array
        try {
            File file = new File(fileInfo.get().getPath()); // Get the file object
            byte[] fileBytes = convertFileToByteArray(file); // Convert to byte array
            fileDetail.put("content", fileBytes); // Add the byte array to details
        } catch (IOException ioException) {
            logger.error("IO exception while reading the file: {}", ioException.getMessage());
            throw new Exception("Error reading file content", ioException);
        }
        return new AppResponse(BarcoUtil.SUCCESS, "File fetched successfully.", fileDetail);
    }

    /**
     * Method use to delete file by id
     * @param fileId
     * @return AppResponse
     * @throws Exception
     * */
    @Override
    public void deleteFileById(Long fileId) throws Exception {
        logger.info("FileInfoServiceImpl :: deleteFileById -> call fileId={}", fileId);
        Optional<FileInfo> fileInfo = this.fileInfoRepository.findById(fileId);
        // Check if the file exists
        if (!fileInfo.isPresent()) {
            throw new NoSuchElementException("File not found with ID: " + fileId);
        }
        fileInfo.get().setStatus(Status.Delete);
        // file info delete
        this.fileInfoRepository.save(fileInfo.get());
        // delete the audit logs
        this.auditLogRepository.updateStatusByFileId(Status.Delete, fileInfo.get());
    }

    /**
     * Method use to run the file by id
     * @param fileId
     * */
    @Override
    public void runFileById(Long fileId) throws Exception {
        logger.info("FileInfoServiceImpl :: runFileById -> call fileId={}", fileId);
        Optional<FileInfo> fileInfo = this.fileInfoRepository.findById(fileId);
        // Check if the file exists
        if (!fileInfo.isPresent()) {
            throw new NoSuchElementException("File not found with ID: " + fileId);
        }
        fileInfo.get().setFileStatus(FileStatus.Queue);
        this.fileInfoRepository.save(fileInfo.get());
        // Create and save the audit log
        AuditLog auditLog = new AuditLog();
        auditLog.setLogsDetail(String.format("Process change for [%s] [Pending => Queue] status.", fileInfo.get().getFilename()));
        auditLog.setFileInfo(fileInfo.get());
        this.auditLogRepository.save(auditLog);
    }

    /**
     * Method use to process single file
     * @param folderPath
     * @param newFile
     * **/
    private void processSingleFile(String folderPath, MultipartFile newFile) {
        try {
            String filename = newFile.getOriginalFilename();
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFolder(folderPath);
            fileInfo.setPath(folderPath.concat("/" + filename));
            fileInfo.setFilename(filename);
            fileInfo.setRequestId(UUID.randomUUID().toString());
            fileInfo.setType(this.getDocumentType(newFile));
            fileInfo.setFileStatus(FileStatus.Pending);
            fileInfo.setStatus(Status.Active);
            this.efsFileExchange.saveFile(convertFileToByteArrayOutputStream(newFile), fileInfo.getPath());
            fileInfo = this.fileInfoRepository.save(fileInfo);
            AuditLog auditLog = new AuditLog();
            auditLog.setLogsDetail(String.format("%s file saved with pending status.", fileInfo.getFilename()));
            auditLog.setFileInfo(fileInfo);
            this.auditLogRepository.save(auditLog);
        } catch (Exception ex) {
            logger.error("Error while processing file: {}", newFile.getOriginalFilename(), ex);
            throw new RuntimeException("File processing failed for: " + newFile.getOriginalFilename(), ex);
        }
    }

    /**
     * Method use to return the file info dto
     * @param fileInfo
     * @return FileInfoDto
     * */
    private FileInfoDto getFileInfoDto(FileInfo fileInfo) {
        FileInfoDto fileInfoDto = new FileInfoDto();
        fileInfoDto.setId(fileInfo.getId());
        fileInfoDto.setRequestId(fileInfo.getRequestId());
        fileInfoDto.setFolder(fileInfo.getFolder());
        fileInfoDto.setFilename(fileInfo.getFilename());
        fileInfoDto.setType(fileInfo.getType());
        fileInfoDto.setSegmentPath(fileInfo.getSegmentPath());
        fileInfoDto.setPath(fileInfo.getPath());
        fileInfoDto.setFileStatus(fileInfo.getFileStatus());
        fileInfoDto.setStatus(fileInfo.getStatus());
        return fileInfoDto;
    }

    /**
     * Method use to return the audit log info
     * @param auditLog
     * @return AuditLogDto
     * */
    private AuditLogDto getAuditLogDto(AuditLog auditLog) {
        AuditLogDto auditLogDto = new AuditLogDto();
        auditLogDto.setId(auditLog.getId());
        auditLogDto.setDateCreated(auditLog.getDateCreated());
        auditLogDto.setLogsDetail(auditLog.getLogsDetail());
        return auditLogDto;
    }

    /**
     * Method use to check the file type
     * @param files
     * @return boolean
     * */
    private boolean isValidFileType(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            String contentType = file.getContentType();
            if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
                // Return false immediately if any file has an invalid content type
                return false;
            }
        }
        // Return true only if all files are valid
        return true;
    }

    /**
     * Method use to get the doc type
     * @param file
     * */
    private String getDocumentType(MultipartFile file) {
        switch (Objects.requireNonNull(file.getContentType())) {
            case "text/csv":
                return "CSV";
            case "text/plain":
                return "TXT";
            case "application/x-parquet":
            case "application/octet-stream":
                return "PARQUET";
            default:
                return null; // Invalid type
        }
    }

    /**
     * Method use to convert file to byte array
     * @param file
     * @throws IOException
     * */
    private ByteArrayOutputStream convertFileToByteArrayOutputStream(MultipartFile file) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = file.getInputStream()) {
            byte[] buffer = new byte[1024]; // Buffer size can be adjusted
            int bytesRead;
            // Read from the InputStream and write to the ByteArrayOutputStream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        }
        return byteArrayOutputStream;
    }

    // Method to convert File to byte array
    private byte[] convertFileToByteArray(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            // Read the file content
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            // Convert ByteArrayOutputStream to byte array
            return byteArrayOutputStream.toByteArray();
        }
    }
}
