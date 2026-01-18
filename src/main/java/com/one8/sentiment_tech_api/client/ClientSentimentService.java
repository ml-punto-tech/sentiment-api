package com.one8.sentiment_tech_api.client;

import com.one8.sentiment_tech_api.dtos.request.SentimentRequestDTO;
import com.one8.sentiment_tech_api.dtos.response.SentimentResponseDTO;
import com.one8.sentiment_tech_api.exceptions.ServiceUnavailableException;
import com.one8.sentiment_tech_api.service.SentimentService;
import com.one8.sentiment_tech_api.service.SentimentStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * Servicio que media entre el controlador y se comunica con el ms de python
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientSentimentService implements SentimentService {

    private final RestClient restClient;
    private final SentimentStatsService sentimentStatsService;

    @Override
    public SentimentResponseDTO predict(SentimentRequestDTO request) {
        log.info("Iniciando análisis de sentimiento para el texto: {}", request.text());

        try {
            // llamada al microservicio de Python (FastAPI/Flask)
            SentimentResponseDTO response = restClient.post()
                    .uri("predict") // Ajustar según el endpoint de Python
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(SentimentResponseDTO.class);
            
            // Guardar automáticamente el resultado en la base de datos
            sentimentStatsService.saveLog(request.text(), response);
            
            return response;
        } catch (Exception e) {
            log.error("Error al conectar con el modelo de Data Science: {}", e.getMessage());
            throw new ServiceUnavailableException("El servicio de análisis no está disponible en este momento.");
        }
    }
}
