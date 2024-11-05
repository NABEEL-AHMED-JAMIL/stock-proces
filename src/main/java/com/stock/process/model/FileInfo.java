package com.stock.process.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import com.stock.process.enums.FileStatus;
import com.stock.process.enums.Status;
import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

/**
 * @author Nabeel Ahmed
 */
@Entity
@Table(name = "file_info")
@SqlResultSetMapping(
    name = "StatisticDtoMapping",
    columns = {
        @ColumnResult(name = "date", type = LocalDate.class),
        @ColumnResult(name = "fileStatus", type = FileStatus.class),
        @ColumnResult(name = "totalCount", type = Long.class)
    }
)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileInfo {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // same request id can be same with multiple
    @Column(name = "request_id")
    private String requestId;

    @Column(name = "folder", nullable = false)
    private String folder;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "segment_path")
    private String segmentPath;

    @Column(name = "file_status")
    @Enumerated(EnumType.ORDINAL)
    private FileStatus fileStatus;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @Column(name = "date_created", nullable = false)
    private Timestamp dateCreated;

    @PrePersist
    public void onCreate() {
        this.status = Status.Active;
        this.dateCreated = new Timestamp((System.currentTimeMillis()));
    }

    public FileInfo() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSegmentPath() {
        return segmentPath;
    }

    public void setSegmentPath(String segmentPath) {
        this.segmentPath = segmentPath;
    }

    public FileStatus getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(FileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
