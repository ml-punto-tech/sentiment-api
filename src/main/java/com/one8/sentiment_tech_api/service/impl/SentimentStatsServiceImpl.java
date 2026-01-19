package com.one8.sentiment_tech_api.service.impl;

import com.one8.sentiment_tech_api.dtos.response.SentimentResponseDTO;
import com.one8.sentiment_tech_api.dtos.response.SentimentStatsResponseDTO;
import com.one8.sentiment_tech_api.entity.SentimentLog;
import com.one8.sentiment_tech_api.repository.SentimentRepository;
import com.one8.sentiment_tech_api.service.SentimentStatsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SentimentStatsServiceImpl implements SentimentStatsService {

    private final SentimentRepository repository;

    @Override
    @Transactional
    public void saveLog(String text,SentimentResponseDTO responseDTO) {
        log.info("Guardando resultado");

        SentimentLog logSentiment = SentimentLog.builder().text(text)
                        .prediction(responseDTO.prevision())
                        .probability(responseDTO.probabilidad())
                        .createdAt(LocalDateTime.now()).build();

         repository.save(logSentiment);

         log.info("Sentimiento guardado correctamente");
    }

    @Override
    public SentimentStatsResponseDTO getStats(int limit){
        log.info("Calculando estadísticas de los últimos {} registros",limit);

        Pageable pageable = PageRequest.of(0, limit);
        List<SentimentLog> logs = repository.findAllByOrderByCreatedAtDesc(pageable);

        if(logs.isEmpty()){
            return new SentimentStatsResponseDTO(0,0,0,0,0.0,0.0,0.0);
        }
        long total = logs.size();

        Map<String, Long> conteos = logs.stream()
                .collect(Collectors.groupingBy(log -> log.getPrediction()
                .trim().toLowerCase(), Collectors.counting()));

        long positivos = conteos.getOrDefault("positivo", 0L);
        long negativos = conteos.getOrDefault("negativo", 0L);
        long neutros = conteos.getOrDefault("neutral", 0L);

        double porcentajePositivos = (positivos * 100.0) / total;
        double porcentajeNegativos = (negativos * 100.0) / total;
        double porcentajeNeutros = (neutros * 100.0) / total;

        return new SentimentStatsResponseDTO(total,positivos,negativos,neutros,
                porcentajePositivos,porcentajeNegativos,porcentajeNeutros);

    }
}
