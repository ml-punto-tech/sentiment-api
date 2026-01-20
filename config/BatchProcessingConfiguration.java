package com.one8.sentiment_tech_api.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.util.unit.DataSize;

import jakarta.servlet.MultipartConfigElement;

@Configuration
@EnableConfigurationProperties(BatchProcessingProperties.class)
@RequiredArgsConstructor
@Slf4j
public class BatchProcessingConfiguration {

    private final BatchProcessingProperties properties;

    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(properties.getMaxFileSize());
        factory.setMaxRequestSize(properties.getMaxRequestSize());
        return factory.createMultipartConfig();
    }
}