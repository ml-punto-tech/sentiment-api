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

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(nullable = false)
    private String prediction;

    @Column(nullable = false)
    private double probability;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}
