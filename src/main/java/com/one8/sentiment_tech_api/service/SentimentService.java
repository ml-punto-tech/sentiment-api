package com.one8.sentiment_tech_api.service;

import com.one8.sentiment_tech_api.dtos.request.SentimentRequestDTO;
import com.one8.sentiment_tech_api.dtos.response.SentimentResponseDTO;

public interface SentimentService {

    SentimentResponseDTO predict(SentimentRequestDTO request);
}
