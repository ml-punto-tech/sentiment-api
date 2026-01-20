package com.one8.sentiment_tech_api.service.impl;

import com.one8.sentiment_tech_api.config.BatchProcessingProperties;
import com.one8.sentiment_tech_api.dtos.request.SentimentRequestDTO;
import com.one8.sentiment_tech_api.dtos.response.SentimentResponseDTO;
import com.one8.sentiment_tech_api.dtos.response.TextSentimentResultDTO;
import com.one8.sentiment_tech_api.exceptions.CsvProcessingException;
import com.one8.sentiment_tech_api.exceptions.FileSizeExceededException;
import com.one8.sentiment_tech_api.service.CsvProcessingService;
import com.one8.sentiment_tech_api.service.SentimentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvProcessingServiceImpl implements CsvProcessingService {

    private final BatchProcessingProperties properties;

    @Override
    public void validateCsvFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new CsvProcessingException("El archivo no puede estar vacío");
        }

        if (file.getSize() > properties.getMaxFileSize().toBytes()) {
            throw new FileSizeExceededException("El archivo no puede superar los " + properties.getMaxFileSize().toMegabytes() + "MB");
        }

        String filename = file.getOriginalFilename();
        boolean hasValidExtension = false;
        for (String extension : properties.getCsvExtensions()) {
            if (filename != null && filename.toLowerCase().endsWith(extension)) {
                hasValidExtension = true;
                break;
            }
        }
        
        if (!hasValidExtension) {
            throw new CsvProcessingException("El archivo debe tener una de estas extensiones: " + String.join(", ", properties.getCsvExtensions()));
        }
    }

    @Override
    public List<String> extractTextsFromCsv(MultipartFile file) throws Exception {
        List<String> texts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                
                if (line.isEmpty()) {
                    continue;
                }
                
                if (lineNumber == 1 && isHeaderLine(line)) {
                    log.info("Omitiendo línea de encabezado: {}", line);
                    continue;
                }
                
                String text = cleanText(line);
                if (text.length() >= properties.getMinTextLength()) {
                    texts.add(text);
                } else {
                    log.warn("Texto omitido por tener menos de {} caracteres (línea {}): {}", properties.getMinTextLength(), lineNumber, text);
                }
            }
        } catch (IOException e) {
            throw new CsvProcessingException("Error leyendo el archivo CSV: " + e.getMessage());
        }

        if (texts.isEmpty()) {
            throw new CsvProcessingException("No se encontraron textos válidos (mínimo " + properties.getMinTextLength() + " caracteres) en el archivo CSV");
        }

        log.info("Se extrajeron {} textos válidos del archivo CSV", texts.size());
        return texts;
    }

    @Override
    public List<TextSentimentResultDTO> processBatchSentimentsWithText(List<String> texts, SentimentService sentimentService) {
        return processBatchWithMapper(
                texts, sentimentService, TextSentimentResultDTO::new,
                text -> new TextSentimentResultDTO(text, new SentimentResponseDTO("ERROR", 0.0))
        );
    }

    private <T> List<T> processBatchWithMapper(List<String> texts, SentimentService sentimentService,
                                             ResultMapper<T> successMapper, ErrorMapper<T> errorMapper) {
        List<T> results = new ArrayList<>();
        
        for (int i = 0; i < texts.size(); i++) {
            String text = texts.get(i);
            try {
                SentimentRequestDTO request = new SentimentRequestDTO(text);
                SentimentResponseDTO response = sentimentService.predict(request);
                results.add(successMapper.map(text, response));
                
                log.info("Procesado texto {}/{} - Sentimiento: {}", 
                    i + 1, texts.size(), response.prevision());
                
            } catch (Exception e) {
                log.error("Error procesando texto {}/{}: {}", 
                    i + 1, texts.size(), e.getMessage());
                
                results.add(errorMapper.map(text));
            }
        }
        
        return results;
    }

    @FunctionalInterface
    private interface ResultMapper<T> {
        T map(String text, SentimentResponseDTO response);
    }

    @FunctionalInterface
    private interface ErrorMapper<T> {
        T map(String text);
    }

    private boolean isHeaderLine(String line) {
        String[] headers = {"texto", "text", "mensaje", "message", "comentario", "comment", "feedback"};
        String lowerLine = line.toLowerCase();
        
        for (String header : headers) {
            if (lowerLine.contains(header)) {
                return true;
            }
        }
        return false;
    }

    private String cleanText(String text) {
        if (text.startsWith("\"") && text.endsWith("\"")) {
            text = text.substring(1, text.length() - 1);
        }
        
        text = text.replace("\"\"", "\"");
        
        return text.trim();
    }
}