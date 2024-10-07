package com.stock.process.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.*;

/**
 * @author Nabeel Ahmed
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private Logger logger = LoggerFactory.getLogger(SwaggerConfig.class);

    /**
     * Method use to add the api docket detail
     * @return Docket
     * */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
            .apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build();
    }

    /**
     * Method use to add the api docket detail
     * @return Docket
     * */
    private ApiInfo apiInfo() {
        return new ApiInfo("Batch Process API", "Batch Process Api.","1.0","Terms of service",
            new Contact("Nabeel Ahmed", "www.process.com", "nabeel.amd93@gmail.com"), "License of API", "API license URL", Collections.emptyList());
    }

}