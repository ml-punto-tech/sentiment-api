package com.one8.sentiment_tech_api.service;

import com.one8.sentiment_tech_api.dtos.response.BatchSentimentResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface BatchSentimentService {
    BatchSentimentResponseDTO processBatchFromCsv(MultipartFile file);
}