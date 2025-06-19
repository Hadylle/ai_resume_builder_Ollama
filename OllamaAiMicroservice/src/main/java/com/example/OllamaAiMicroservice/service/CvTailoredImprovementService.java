package com.example.OllamaAiMicroservice.service;

import com.example.OllamaAiMicroservice.model.CvAnalysisResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CvTailoredImprovementService {

    private final OllamaChatModel chatModel;
    private final ObjectMapper objectMapper;

    public CvAnalysisResult tailorCvToJob(String cvText, String jobDescription) {
        String prompt = """
                You are an AI assistant that improves and tailors CVs for job applications.

                Given the resume between the first --- block, and the job description between the second --- block:
                ---
                %s
                ---
                %s
                ---

                1. Extract all CV data into structured JSON like the example below.
                2. Improve grammar and phrasing.
                3. Add missing but relevant information based on the job description (keywords, tools, responsibilities).
                4. Ensure that the CV seems aligned with the job to maximize hiring potential.
                5. Add any missing skills or experiences that are reasonable and match the candidate profile.

                Return the improved result in this **strict JSON format** (don't change field names or structure!):

                {
                  "name": "Full Name",
                  "email": "email@example.com",
                  "phone": "+123456789",
                  "address": "123 Main St, City",
                  "portfolio": "https://portfolio.com",
                  "linkedin": "https://linkedin.com/in/username",
                  "github": "https://github.com/username",
                  "aboutMe": "Brief improved and aligned summary",
                  "skills": ["Skill1", "Skill2"],
                  "experience": [
                    {
                      "company": "Company Name",
                      "role": "Job Title",
                      "duration": "Jan 2020 - Dec 2022",
                      "achievements": ["Did X", "Did Y"],
                      "techStack": ["Java", "React"]
                    }
                  ],
                  "education": [
                    {
                      "institution": "University Name",
                      "degree": "BSc",
                      "field": "Computer Science",
                      "startDate": "Sep 2019",
                      "endDate": "June 2023"
                    }
                  ],
                  "projects": [
                    {
                      "title": "Project Name",
                      "description": "Improved and aligned description",
                      "techStack": ["React", "Spring Boot"],
                      "link": "https://github.com/project"
                    }
                  ],
                  "certifications": [
                    {
                      "title": "Certification Name",
                      "issuer": "Organization",
                      "year": "2023"
                    }
                  ],
                  "languages": ["English", "French"],
                  "interests": ["Reading", "Hiking"],
                  "socialClubs": ["Robotics Club"]
                }

                Only return the improved JSON object. No commentary or extra output. All brackets must be closed.
                """.formatted(cvText, jobDescription);

        OllamaOptions options = new OllamaOptions();
        options.setModel("llama3");
        options.setTemperature(0.9);

        ChatResponse response = chatModel.call(new Prompt(prompt, options));

        try {
            String raw = response.getResult().getOutput().getText();
            String json = extractJson(raw);
            return objectMapper.readValue(json, CvAnalysisResult.class);
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to parse tailored CV JSON: " + e.getMessage(), e);
        }
    }

    private String extractJson(String input) {
        int start = input.indexOf('{');
        int end = input.lastIndexOf('}');
        if (start != -1 && end != -1 && end > start) {
            return input.substring(start, end + 1);
        }
        throw new RuntimeException("No JSON object found in AI response");
    }
}
