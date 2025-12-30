package com.one8.sentiment_tech_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientConfig {

    @Value("${model.api.url}")
    private String modelApiUrl;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(modelApiUrl)
                .build();
    }
}
