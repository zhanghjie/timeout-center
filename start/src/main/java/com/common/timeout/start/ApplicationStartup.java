package com.common.timeout.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * com.common.timeout.start.ApplicationStartup
 * 功能描述: todo
 *
 * @author zhanghaojie
 * @date 2021/11/10 10:54
 */
@SpringBootApplication
@ImportResource(locations = {"classpath:application-config.xml"})
@EnableScheduling
@EnableAsync
public class ApplicationStartup extends SpringBootServletInitializer {


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ApplicationStartup.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ApplicationStartup.class, args);
    }

}
