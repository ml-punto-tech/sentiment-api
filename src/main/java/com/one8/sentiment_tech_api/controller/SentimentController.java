package com.one8.sentiment_tech_api.controller;

import com.one8.sentiment_tech_api.dtos.request.SentimentRequestDTO;
import com.one8.sentiment_tech_api.dtos.response.ApiResponse;
import com.one8.sentiment_tech_api.dtos.response.SentimentResponseDTO;
import com.one8.sentiment_tech_api.dtos.response.SentimentStatsResponseDTO;
import com.one8.sentiment_tech_api.entity.SentimentLog;
import com.one8.sentiment_tech_api.service.SentimentService;
import com.one8.sentiment_tech_api.service.SentimentStatsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la gestión de sentimientos.
 * Endpoints: /api/v1/sentiment
 */
@Slf4j
@RestController
@RequestMapping("sentiment")
@RequiredArgsConstructor
public class SentimentController {

    private final SentimentService sentimentService;
    private final SentimentStatsService sentimentStatsService;

    /**
     * POST /api/v1/sentiment
     * Endpoint para obtener el sentimiento de un feedback.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<SentimentResponseDTO>> analyzeAndSave(@Valid @RequestBody SentimentRequestDTO request) {
        log.info("POST /api/v1/sentiment  {}", request);

        SentimentResponseDTO predictionResponse = sentimentService.predict(request);

        log.info("Guardando");
        sentimentStatsService.saveLog(request.text(),predictionResponse);

        ApiResponse<SentimentResponseDTO> response = ApiResponse.<SentimentResponseDTO>builder()
                .success(true)
                .data(predictionResponse)
                .message("Predicción recibida exitosamente")
                .build();

        return ResponseEntity.ok(response);
    }

    //Devuelve stats
    @GetMapping("/stats")
    public ResponseEntity<SentimentStatsResponseDTO>getStats(@RequestParam(defaultValue = "10") int last){
        log.info("GET /sentiment/stats?last={}", last);

        SentimentStatsResponseDTO stats = sentimentStatsService.getStats(last);
        return ResponseEntity.ok(stats);
    }

}
