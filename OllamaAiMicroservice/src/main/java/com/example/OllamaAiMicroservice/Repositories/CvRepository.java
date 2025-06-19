package com.example.OllamaAiMicroservice.Repositories;

import com.example.OllamaAiMicroservice.entity.CvEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CvRepository extends JpaRepository<CvEntity, Long> {}
