package com.one8.sentiment_tech_api.service;

import com.one8.sentiment_tech_api.dtos.response.SentimentResponseDTO;
import com.one8.sentiment_tech_api.dtos.response.SentimentStatsResponseDTO;

public interface SentimentStatsService {

    void saveLog(String text, SentimentResponseDTO responseDTO);

    SentimentStatsResponseDTO getStats(int limit);
}
