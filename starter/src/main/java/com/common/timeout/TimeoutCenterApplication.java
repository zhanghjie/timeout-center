package com.common.timeout;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@MapperScan("")
@ImportResource(locations = {"classpath:application-config.xml"})
@EnableScheduling
@EnableOpenApi
public class TimeoutCenterApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TimeoutCenterApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(TimeoutCenterApplication.class, args);
    }

}
