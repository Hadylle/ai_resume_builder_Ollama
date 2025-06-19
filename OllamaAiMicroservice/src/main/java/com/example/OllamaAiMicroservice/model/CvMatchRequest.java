package com.example.OllamaAiMicroservice.model;

import lombok.Data;

@Data
public class CvMatchRequest {
    private String cv_text;
    private String job_description;
}
