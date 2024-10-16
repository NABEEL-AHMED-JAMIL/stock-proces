package com.stock.process.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import com.google.gson.Gson;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Nabeel Ahmed
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"file", "files", "data"})
public class FileUploadRequest<T> {

    @JsonProperty("file")
    private MultipartFile file;
    @JsonProperty("files")
    private List<MultipartFile> files;

    public FileUploadRequest() {
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

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}