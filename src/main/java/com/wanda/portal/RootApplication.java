package com.wanda.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Created by chenyuanjun
 */
@EnableAutoConfiguration
@EnableCaching
@SpringBootApplication
public class RootApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RootApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(RootApplication.class, args);
    }

}
