package com.one8.sentiment_tech_api.controller;

import com.one8.sentiment_tech_api.dtos.request.SentimentRequestDTO;
import com.one8.sentiment_tech_api.dtos.response.ApiResponse;
import com.one8.sentiment_tech_api.dtos.response.BatchSentimentResponseDTO;
import com.one8.sentiment_tech_api.dtos.response.SentimentResponseDTO;
import com.one8.sentiment_tech_api.dtos.response.SentimentStatsResponseDTO;
import com.one8.sentiment_tech_api.service.BatchSentimentService;
import com.one8.sentiment_tech_api.service.SentimentService;
import com.one8.sentiment_tech_api.service.impl.SentimentStatsServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controlador REST para la gestión de sentimientos.
 * Endpoints: /api/v1/sentiment
 */
@Validated
@Slf4j
@RestController
@RequestMapping("sentiment")
@RequiredArgsConstructor
public class SentimentController {

    private final SentimentService sentimentService;
    private final SentimentStatsServiceImpl sentimentStatsService;
    private final BatchSentimentService batchSentimentService;

    /**
     * POST /api/v1/sentiment
     * Endpoint para obtener el sentimiento de un feedback.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<SentimentResponseDTO>> analyze(@Valid @RequestBody SentimentRequestDTO request) {
        log.info("POST /api/v1/sentiment  {}", request);

        SentimentResponseDTO predictionResponse = sentimentService.predict(request);

        ApiResponse<SentimentResponseDTO> response = ApiResponse.<SentimentResponseDTO>builder()
                .success(true)
                .data(predictionResponse)
                .message("Predicción recibida exitosamente")
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/v1/sentiment/batch
     * Endpoint para procesar un archivo CSV con múltiples textos y obtener sus sentimientos.
     */
    @PostMapping(value = "batch", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<BatchSentimentResponseDTO>> analyzeBatch(
            @RequestParam("file") MultipartFile file) {

        log.info("POST /api/v1/sentiment/batch - Procesando archivo: {}", file.getOriginalFilename());

        BatchSentimentResponseDTO batchResponse = batchSentimentService.processBatchFromCsv(file);

        ApiResponse<BatchSentimentResponseDTO> response = ApiResponse.<BatchSentimentResponseDTO>builder()
                .success(true)
                .data(batchResponse)
                .message(String.format("Se procesaron %d textos del archivo CSV (%d exitosos, %d fallidos). Sentimientos: %d positivos, %d neutrales, %d negativos",
                        batchResponse.totalProcessed(), batchResponse.successful(), batchResponse.failed(),
                        batchResponse.totalPositives(), batchResponse.totalNeutrals(), batchResponse.totalNegatives()))
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/sentiment/stats
     * Endpoint para obtener estadísticas de los últimos análisis de sentimiento realizados.
     * Permite consultar las estadísticas de las últimas N predicciones, donde N es un valor
     * entre 5 y 100 (por defecto 10).
     */
    @GetMapping("/stats")
    public ResponseEntity<SentimentStatsResponseDTO>getStats(
            @RequestParam(defaultValue = "10")
            @Min(value = 5, message = "Debe haber al menos 5 últimas predicciones")
            @Max(value = 100, message ="No puede exceder 100 últimas predicciones")
            int last){
        log.info("GET /sentiment/stats?last={}", last);

        SentimentStatsResponseDTO stats = sentimentStatsService.getStats(last);
        return ResponseEntity.ok(stats);
    }

}
