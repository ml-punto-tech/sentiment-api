package com.one8.sentiment_tech_api.repository;

import com.one8.sentiment_tech_api.entity.SentimentLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface SentimentRepository extends JpaRepository<SentimentLog, Long> {

    Page<SentimentLog> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
