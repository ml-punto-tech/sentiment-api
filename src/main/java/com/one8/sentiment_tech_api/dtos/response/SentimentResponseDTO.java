package com.one8.sentiment_tech_api.dtos.response;


public record SentimentResponseDTO(

        String prevision,
        double probabilidad
) {
}
