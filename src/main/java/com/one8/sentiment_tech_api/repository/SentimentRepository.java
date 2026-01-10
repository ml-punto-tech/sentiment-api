package com.one8.sentiment_tech_api.repository;

import com.one8.sentiment_tech_api.entity.SentimentLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SentimentRepository extends JpaRepository<SentimentLog, Long> {
}
