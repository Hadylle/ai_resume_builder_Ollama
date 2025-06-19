package com.example.OllamaAiMicroservice.service;

import com.example.OllamaAiMicroservice.model.CvMatchResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CvMatchingService {

    private final OllamaChatModel chatModel; // or ChatModel for generic approach
    private final ObjectMapper objectMapper;

    public CvMatchResult matchCvToJob(String cvText, String jobText) {
        String prompt = """
    You are a CV to Job Matcher.

    Given the following resume:
    ---
    %s
    ---

    And the following job description:
    ---
    %s
    ---

    Provide a similarity score between 0 and 100 indicating how well the resume matches the job.

    Also provide these fields explaining your score:

    - "scoreExplanation": A brief explanation why you gave this score.
    - "missingSkills": What important skills or qualifications are missing from the resume.
    - "commonSkills": What skills or qualifications are common between the CV and job description.
    - "jobFit": A short conclusion whether this job fits the candidate or not (yes/no).

    Respond only in JSON format, for example:

    {
      "similarity": 78,
      "scoreExplanation": "The CV matches most technical skills but lacks leadership experience.",
      "missingSkills": "Leadership, project management",
      "commonSkills": "Java, Spring Boot, REST APIs",
      "jobFit": "yes"
    }
    """.formatted(cvText, jobText);

        OllamaOptions options = new OllamaOptions();
        options.setModel("llama3");
        options.setTemperature(0.9);  // Slightly higher for matching tasks

        ChatResponse response = chatModel.call(new Prompt(prompt, options));
        try {
            String content = response.getResult().getOutput().getText();
            JsonNode node = objectMapper.readTree(content);

            CvMatchResult result = new CvMatchResult();
            result.setSimilarity(node.get("similarity").asDouble());
            result.setScoreExplanation(node.get("scoreExplanation").asText());
            result.setMissingSkills(node.get("missingSkills").asText());
            result.setCommonSkills(node.get("commonSkills").asText());
            result.setJobFit(node.get("jobFit").asText());

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse similarity score from AI response", e);
        }
    }
}