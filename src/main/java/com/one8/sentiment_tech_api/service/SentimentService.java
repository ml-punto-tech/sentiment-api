package com.one8.sentiment_tech_api.service;

import com.one8.sentiment_tech_api.dtos.request.SentimentRequestDTO;
import com.one8.sentiment_tech_api.dtos.response.SentimentResponseDTO;

public interface SentimentService {

    /**
     * Analiza el sentimiento del texto y guarda el resultado en la base de datos.
     * @param request DTO con el texto a analizar
     * @return respuesta con la predicción y probabilidad
     */
    SentimentResponseDTO predict(SentimentRequestDTO request);
}
