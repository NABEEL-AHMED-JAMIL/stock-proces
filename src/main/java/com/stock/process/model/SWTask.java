package com.stock.process.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import com.stock.process.enums.Status;
import com.stock.process.enums.TaskStatus;
import com.stock.process.enums.TaskType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Nabeel Ahmed
 */
@Entity
@Table(name = "sw_task")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SWTask {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "task_type", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private TaskType taskType;

    @Column(name = "task_status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private TaskStatus taskStatus;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String jsonPayload;

    @OneToMany(mappedBy = "swTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SwTaskAuditLog> swTaskAuditLogs;

    @OneToMany(mappedBy = "swTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SwTaskFile> swTaskFiles;

    // task to user account
    @ManyToOne
    @JoinColumn(name = "sw_user_id", nullable = false)
    private SwUser swUser;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @PrePersist
    public void onCreate() {
        this.status = Status.Active;
        this.createdAt = new Timestamp((System.currentTimeMillis()));
    }

    public SWTask() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getJsonPayload() {
        return jsonPayload;
    }

    public void setJsonPayload(String jsonPayload) {
        this.jsonPayload = jsonPayload;
    }

    public List<SwTaskAuditLog> getSwTaskAuditLogs() {
        return swTaskAuditLogs;
    }

    public void setSwTaskAuditLogs(List<SwTaskAuditLog> swTaskAuditLogs) {
        this.swTaskAuditLogs = swTaskAuditLogs;
    }

    public List<SwTaskFile> getSwTaskFiles() {
        return swTaskFiles;
    }

    public void setSwTaskFiles(List<SwTaskFile> swTaskFiles) {
        this.swTaskFiles = swTaskFiles;
    }

    public SwUser getSwUser() {
        return swUser;
    }

    public void setSwUser(SwUser swUser) {
        this.swUser = swUser;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
