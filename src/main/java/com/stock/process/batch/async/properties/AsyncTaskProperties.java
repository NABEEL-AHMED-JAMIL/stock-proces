package com.stock.process.batch.async.properties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Nabeel Ahmed
 */
@Component
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AsyncTaskProperties {

    @Value("${async.task.executor.corePoolSize}")
    private Integer corePoolSize;
    @Value("${async.task.executor.maximumPoolSize}")
    private Integer maximumPoolSize;
    @Value("${async.task.executor.keepAliveTime}")
    private Integer keepAliveTime;

    public AsyncTaskProperties() {}

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public Integer getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(Integer keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}