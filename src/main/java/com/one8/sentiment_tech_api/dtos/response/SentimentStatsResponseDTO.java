package com.one8.sentiment_tech_api.dtos.response;


public record SentimentStatsResponseDTO(
        long total,
        long positivos,
        long negativos,
        long neutros,
        double porcentajePositivos,
        double porcentajeNegativos,
        double porcentajeNeutros
) {
}
