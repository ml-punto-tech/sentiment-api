package com.one8.sentiment_tech_api.service;

import com.one8.sentiment_tech_api.dtos.request.SentimentRequestDTO;
import com.one8.sentiment_tech_api.dtos.response.SentimentResponseDTO;
import com.one8.sentiment_tech_api.exceptions.ServiceUnavaliableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * Servicio que media entre el controlador y se comunica con el ms de python
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SentimentService {

    private final RestClient restClient;

    public SentimentResponseDTO predict(SentimentRequestDTO request) {
        log.info("Iniciando análisis de sentimiento para el texto: {}", request.text());

        try {
            // llamada al microservicio de Python (FastAPI/Flask) Descomentar cuando tengamos la uri del ms
//            SentimentResponseDTO response = restClient.post()
//                    .uri("predict") // Ajustar según el endpoint de Python
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body(request)
//                    .retrieve()
//                    .body(SentimentResponseDTO.class);
//
//            return response;
            return new SentimentResponseDTO("Negativa", 0.1);
        } catch (Exception e) {
            log.error("Error al conectar con el modelo de Data Science: {}", e.getMessage());
            throw new ServiceUnavaliableException("El servicio de análisis no está disponible en este momento.");
        }
    }
}
