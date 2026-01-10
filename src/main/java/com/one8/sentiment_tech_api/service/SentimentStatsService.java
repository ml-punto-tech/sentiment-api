package com.one8.sentiment_tech_api.service;


import com.one8.sentiment_tech_api.dtos.request.SentimentRequestDTO;
import com.one8.sentiment_tech_api.dtos.response.SentimentResponseDTO;
import com.one8.sentiment_tech_api.entity.SentimentLog;
import com.one8.sentiment_tech_api.repository.SentimentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class SentimentStatsService{

    private final SentimentService sentimentService;
    private final SentimentRepository repository;


    public SentimentResponseDTO analyzeAndSave(SentimentRequestDTO request) {
        log.info("Analizando sentimiento y guardando resultado");

        SentimentResponseDTO response = sentimentService.predict(request);

        SentimentLog logEntity = SentimentLog.builder().text(request.text())
                .prediction(response.prevision()).probability(response.probabilidad())
                .createdAt(LocalDateTime.now()).build();

        repository.save(logEntity);
        log.info("Sentimiento guardado correctamente");
        return response;
    }
}
