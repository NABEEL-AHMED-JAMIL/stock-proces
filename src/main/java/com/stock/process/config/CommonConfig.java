package com.stock.process.config;

import com.stock.process.batch.async.executor.AsyncDALTaskExecutor;
import com.stock.process.batch.async.properties.AsyncTaskProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Nabeel Ahmed
 */
@Configuration
public class CommonConfig {

    public Logger logger = LogManager.getLogger(CommonConfig.class);

    @Autowired
    public AsyncTaskProperties asyncTaskProperties;
    /**
     * Method use to async dal task executor
     * @return AsyncDALTaskExecutor
     * */
    @Bean
    public AsyncDALTaskExecutor asyncDALTaskExecutor() throws Exception {
        logger.debug("===============Application-DAO-INIT===============");
        AsyncDALTaskExecutor taskExecutor = new AsyncDALTaskExecutor(this.asyncTaskProperties.getCorePoolSize(),
            this.asyncTaskProperties.getMaximumPoolSize(), this.asyncTaskProperties.getKeepAliveTime());
        logger.debug("===============Application-DAO-END===============");
        return taskExecutor;
    }

}