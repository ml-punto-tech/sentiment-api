package com.one8.sentiment_tech_api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

@Data
@Component
@ConfigurationProperties(prefix = "app.batch")
public class BatchProcessingProperties {

    private DataSize maxFileSize = DataSize.ofMegabytes(10);
    private DataSize maxRequestSize = DataSize.ofMegabytes(10);
    private int minTextLength = 10;
    private String[] csvExtensions = {".csv"};
    private String[] validContentTypes = {
        "text/csv",
        "application/csv",
        "text/comma-separated-values",
        "application/vnd.ms-excel",
        "application/octet-stream"
    };
}