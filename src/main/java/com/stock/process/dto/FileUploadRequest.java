package com.stock.process.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRawValue;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Nabeel Ahmed
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "file", "files", "data" })
public class FileUploadRequest<T> {

    @JsonProperty("file")
    private MultipartFile file;
    @JsonProperty("files")
    private List<MultipartFile> files;
    private Map<String, MultipartFile> uniqueFiles;
    @JsonRawValue
    @JsonProperty("data")
    private T data;

    public FileUploadRequest() { }

    public FileUploadRequest(T data) { this.data = data; }

    public FileUploadRequest(MultipartFile file, T data) {
        this.file = file;
        this.data = data;
    }

    public FileUploadRequest(List<MultipartFile> files, T data) {
        this.files = files;
        this.data = data;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }

    public Map<String, MultipartFile> getUniqueFiles() {
        return uniqueFiles;
    }

    public void setUniqueFiles(Map<String, MultipartFile> uniqueFiles) {
        this.uniqueFiles = uniqueFiles;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}