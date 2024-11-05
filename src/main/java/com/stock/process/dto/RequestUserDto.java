package com.stock.process.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import com.stock.process.enums.RequestStatus;
import com.stock.process.enums.Status;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nabeel Ahmed
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestUserDto {

    private Long id;
    private String username;
    private String swAccount;
    private RequestStatus requestStatus;
    private List<FileInfoDto> fileInfos = new ArrayList<>();
    private Status status;
    private Timestamp dateCreated;

    public RequestUserDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSwAccount() {
        return swAccount;
    }

    public void setSwAccount(String swAccount) {
        this.swAccount = swAccount;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public List<FileInfoDto> getFileInfos() {
        return fileInfos;
    }

    public void setFileInfos(List<FileInfoDto> fileInfos) {
        this.fileInfos = fileInfos;
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
