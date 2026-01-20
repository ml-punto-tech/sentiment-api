package com.one8.sentiment_tech_api.dtos.response;

import java.util.List;

public record BatchSentimentResponseDTO(

        int totalProcessed,
        int successful,
        int failed,
        int totalPositives,
        int totalNeutrals,
        int totalNegatives,
        List<TextSentimentResultDTO> results
) {
}