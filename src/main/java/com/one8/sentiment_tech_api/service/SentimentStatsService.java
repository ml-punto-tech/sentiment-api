package com.one8.sentiment_tech_api.service;


import com.one8.sentiment_tech_api.dtos.request.SentimentRequestDTO;
import com.one8.sentiment_tech_api.dtos.response.SentimentResponseDTO;
import com.one8.sentiment_tech_api.dtos.response.SentimentStatsResponseDTO;
import com.one8.sentiment_tech_api.entity.SentimentLog;
import com.one8.sentiment_tech_api.repository.SentimentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public SentimentStatsResponseDTO getStats(int limit){
        log.info("Calculando estadísticas de los últimos {} registros",limit);

        Pageable pageable = PageRequest.of(0, limit);
        Page<SentimentLog> logs = repository.findAllByOrderByCreatedAtDesc(pageable);


        if(logs.isEmpty()){
            return new SentimentStatsResponseDTO(0,0,0,0,0.0,0.0,0.0);
        }
        long total = logs.getContent().size();
        long positivos = logs.stream().filter(log -> "POSITIVO".equalsIgnoreCase(log.getPrediction()))
                .count();
        long negativos = logs.stream().filter(log -> "NEGATIVO".equalsIgnoreCase(log.getPrediction()))
                .count();
        long neutros = logs.stream().filter(log -> "NEUTRO".equalsIgnoreCase(log.getPrediction()))
                .count();

        double porcentajePositivos = (positivos * 100.0) / total;
        double porcentajeNegativos = (negativos * 100.0) / total;
        double porcentajeNeutros = (neutros * 100.0) / total;

        return new SentimentStatsResponseDTO(total,positivos,negativos,neutros,
                porcentajePositivos,porcentajeNegativos,porcentajeNeutros);

    }
}
