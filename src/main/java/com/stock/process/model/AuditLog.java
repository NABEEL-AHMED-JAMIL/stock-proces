package com.stock.process.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import com.stock.process.enums.Status;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author Nabeel Ahmed
 */
@Entity
@Table(name = "audit_log")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditLog {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private FileInfo fileInfo;

    @Column(name = "log_detail",
        nullable = false, length = 2500)
    private String logsDetail;

    @Column(name = "date_created",
        nullable = false)
    private Timestamp dateCreated;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    public AuditLog() {}

    @PrePersist
    protected void onCreate() {
        this.status = Status.Active;
        this.dateCreated = new Timestamp(System.currentTimeMillis());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    public String getLogsDetail() {
        return logsDetail;
    }

    public void setLogsDetail(String logsDetail) {
        this.logsDetail = logsDetail;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}