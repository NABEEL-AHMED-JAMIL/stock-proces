package com.stock.process.dto;

import com.stock.process.enums.FileStatus;

/**
 * @author Nabeel Ahmed
 */
public interface StatisticDto {

    public String getDate();

    public FileStatus getFileStatus();

    public Long getTotalCount();

}
