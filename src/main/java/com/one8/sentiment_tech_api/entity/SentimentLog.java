package com.one8.sentiment_tech_api.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sentiment_logs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SentimentLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private String text;

    private String prediction;

    private double probability;

    private LocalDateTime createdAt;

}
