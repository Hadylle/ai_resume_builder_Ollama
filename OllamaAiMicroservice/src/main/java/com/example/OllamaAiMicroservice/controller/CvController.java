package com.example.OllamaAiMicroservice.controller;

import com.example.OllamaAiMicroservice.entity.CvEntity;
import com.example.OllamaAiMicroservice.model.CvAnalysisResult;
import com.example.OllamaAiMicroservice.model.CvMatchResult;
import com.example.OllamaAiMicroservice.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/api/cv")
@RequiredArgsConstructor
public class CvController {

    private final FileParserService fileParserService;
    private final CvAnalysisService cvAnalysisService;
    private final CvMatchingService cvMatchingService;
    private final CvFeedbackService cvFeedbackService;
    private final CvStorageService cvStorageService;
    private final CvTailoredImprovementService tailoringService;

    @Autowired
    private CvImprovementService improvementService;

    @PostMapping("/analyze")
    public ResponseEntity<CvAnalysisResult> analyzeCv(@RequestParam("cv") MultipartFile cvFile) {
        try {
            String cvText = fileParserService.parseFile(cvFile);
            CvAnalysisResult result = cvAnalysisService.analyzeCV(cvText);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/match")
    public ResponseEntity<CvMatchResult> matchCvToJob(
            @RequestParam("cv") MultipartFile cvFile,
            @RequestParam("jobDescription") String jobDescription) {
        try {
            String cvText = fileParserService.parseFile(cvFile);
            CvMatchResult result = cvMatchingService.matchCvToJob(cvText, jobDescription);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping("/review")
    public ResponseEntity<String> reviewCv(@RequestParam("cv") MultipartFile cvFile) {
        try {
            String cvText = fileParserService.parseFile(cvFile);
            String feedback = cvFeedbackService.generateFeedback(cvText);
            return ResponseEntity.ok(feedback);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing CV.");
        }
    }

    @PostMapping(value = "/improve", consumes = "multipart/form-data")
    public ResponseEntity<CvAnalysisResult> improveCv(@RequestParam("file") MultipartFile file) {
        try {
            String cvText = extractTextFromPdf(file.getInputStream());
            CvAnalysisResult improvedCv = improvementService.improveCv(cvText);
            return ResponseEntity.ok(improvedCv);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/tailor")
    public ResponseEntity<CvAnalysisResult> tailorCvToJob(
            @RequestPart("file") MultipartFile file,
            @RequestPart("jobDescription") String jobDescription
    ) {
        try {
            String cvText = extractTextFromPdf(file.getInputStream());
            CvAnalysisResult tailoredResult = tailoringService.tailorCvToJob(cvText, jobDescription);
            return ResponseEntity.ok(tailoredResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/store")
    public ResponseEntity<CvEntity> storeCv(@RequestBody CvAnalysisResult result) {
        CvEntity saved = cvStorageService.saveCv(result);
        return ResponseEntity.ok(saved);
    }

    private String extractTextFromPdf(InputStream inputStream) {
        try {
            Tika tika = new Tika();
            return tika.parseToString(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract text from PDF using Tika", e);
        }
    }
}
