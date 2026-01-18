package com.one8.sentiment_tech_api.service;

import com.one8.sentiment_tech_api.dtos.response.SentimentResponseDTO;
import com.one8.sentiment_tech_api.dtos.response.TextSentimentResultDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CsvProcessingService {

    void validateCsvFile(MultipartFile file);

    List<String> extractTextsFromCsv(MultipartFile file) throws Exception;

    List<TextSentimentResultDTO> processBatchSentimentsWithText(List<String> texts, SentimentService sentimentService);
}