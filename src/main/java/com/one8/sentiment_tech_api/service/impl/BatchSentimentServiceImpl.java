package com.one8.sentiment_tech_api.service.impl;

import com.one8.sentiment_tech_api.dtos.response.BatchSentimentResponseDTO;
import com.one8.sentiment_tech_api.dtos.response.TextSentimentResultDTO;
import com.one8.sentiment_tech_api.exceptions.CsvProcessingException;
import com.one8.sentiment_tech_api.service.BatchSentimentService;
import com.one8.sentiment_tech_api.service.CsvProcessingService;
import com.one8.sentiment_tech_api.service.SentimentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BatchSentimentServiceImpl implements BatchSentimentService {

    private final CsvProcessingService csvProcessingService;
    private final SentimentService sentimentService;

    @Override
    public BatchSentimentResponseDTO processBatchFromCsv(MultipartFile file) {
        log.info("Iniciando procesamiento batch del archivo: {}", file.getOriginalFilename());

        csvProcessingService.validateCsvFile(file);

        try {
            List<String> texts = csvProcessingService.extractTextsFromCsv(file);
            List<TextSentimentResultDTO> results = csvProcessingService.processBatchSentimentsWithText(texts, sentimentService);

            BatchSentimentStatistics statistics = calculateStatistics(results);

            log.info("Procesamiento completado: {}", statistics);

            return new BatchSentimentResponseDTO(
                    statistics.totalProcessed(),
                    statistics.successful(),
                    statistics.failed(),
                    statistics.totalPositives(),
                    statistics.totalNeutrals(),
                    statistics.totalNegatives(),
                    results
            );

        } catch (Exception e) {
            log.error("Error procesando archivo CSV: {}", e.getMessage());
            throw new CsvProcessingException("Error procesando el archivo CSV: " + e.getMessage());
        }
    }

    private BatchSentimentStatistics calculateStatistics(List<TextSentimentResultDTO> results) {
        int successful = (int) results.stream()
                .filter(r -> !"ERROR".equals(r.sentiment().prevision()))
                .count();

        int failed = results.size() - successful;

        int totalPositives = (int) results.stream()
                .filter(r -> !"ERROR".equals(r.sentiment().prevision())
                        && "positivo".equalsIgnoreCase(r.sentiment().prevision()))
                .count();

        int totalNeutrals = (int) results.stream()
                .filter(r -> !"ERROR".equals(r.sentiment().prevision())
                        && "neutral".equalsIgnoreCase(r.sentiment().prevision()))
                .count();

        int totalNegatives = (int) results.stream()
                .filter(r -> !"ERROR".equals(r.sentiment().prevision())
                        && "negativo".equalsIgnoreCase(r.sentiment().prevision()))
                .count();

        return new BatchSentimentStatistics(results.size(), successful, failed, totalPositives, totalNeutrals, totalNegatives);
    }

    private record BatchSentimentStatistics(
            int totalProcessed,
            int successful,
            int failed,
            int totalPositives,
            int totalNeutrals,
            int totalNegatives
    ) {
        @Override
        public String toString() {
            return String.format(
                    "BatchSentimentStatistics{total=%d, successful=%d, failed=%d, positives=%d, neutrals=%d, negatives=%d}",
                    totalProcessed, successful, failed, totalPositives, totalNeutrals, totalNegatives
            );
        }
    }
}