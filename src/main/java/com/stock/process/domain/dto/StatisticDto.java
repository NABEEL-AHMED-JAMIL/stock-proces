package com.stock.process.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.stock.process.domain.enums.FileStatus;

/**
 * @author Nabeel Ahmed
 */
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface StatisticDto {

    public String getDate();

    public FileStatus getFileStatus();

    public String getFileType();

    public Long getTotalCount();

}
