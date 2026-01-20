package com.one8.sentiment_tech_api.dtos.response;

public record TextSentimentResultDTO(
        String text,
        SentimentResponseDTO sentiment
) {
}