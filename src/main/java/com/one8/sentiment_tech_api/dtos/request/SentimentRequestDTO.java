package com.one8.sentiment_tech_api.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SentimentRequestDTO(

        @NotBlank(message = "No puede estar vacio")
        @Size(min =10, message = "Texto mayor a 10 caracteres")
        String text
) {
}
