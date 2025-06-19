package com.example.OllamaAiMicroservice.service;

import com.example.OllamaAiMicroservice.model.CvAnalysisResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CvAnalysisService {

    private final OllamaChatModel chatModel;
    private final ObjectMapper objectMapper;

    public CvAnalysisResult analyzeCV(String cvText) {
        String prompt = """
                You are an AI system that extracts structured information from resumes and returns a strict JSON object.

                Given the resume below between the --- lines, extract the following fields and return a valid, complete JSON object. Every list must be properly opened and closed. Do NOT leave arrays unclosed. Ensure all commas and brackets are in place.

                ---
                %s
                ---

                Return only this object, and follow this format strictly:

                {
                  "name": "Full Name",
                  "email": "email@example.com",
                  "phone": "+123456789",
                  "address": "123 Main St, City",
                  "portfolio": "https://portfolio.com",
                  "linkedin": "https://linkedin.com/in/username",
                  "github": "https://github.com/username",
                  "aboutMe": "Brief summary",
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
                      "description": "Short description of the project",
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

                Only return valid JSON. No extra text. Make sure all brackets are properly closed.
                """.formatted(cvText);

        OllamaOptions options = new OllamaOptions();
        options.setModel("llama3");
        options.setTemperature(0.9);  // Slightly higher for matching tasks

        ChatResponse response = chatModel.call(new Prompt(prompt, options));
        try {
            String content = response.getResult().getOutput().getText();
            String json = extractJson(content);
            System.out.println("🔍 Raw AI output:\n" + content);
            System.out.println("✅ Extracted JSON:\n" + json);

            CvAnalysisResult result = objectMapper.readValue(json, CvAnalysisResult.class);

            // Normalize the result to avoid nulls or missing fields
            normalizeResult(result);

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI response: " + e.getMessage(), e);
        }
    }

    private String extractJson(String input) {
        int start = input.indexOf('{');
        int end = input.lastIndexOf('}');

        if (start != -1 && end != -1 && end > start) {
            return input.substring(start, end + 1);
        } else {
            throw new RuntimeException("No JSON object found in AI response");
        }
    }

    private void normalizeResult(CvAnalysisResult result) {
        if (result == null) return;

        // Normalize String fields to avoid nulls
        if (result.getName() == null) result.setName("");
        if (result.getEmail() == null) result.setEmail("");
        if (result.getPhone() == null) result.setPhone("");
        if (result.getAddress() == null) result.setAddress("");
        if (result.getPortfolio() == null) result.setPortfolio("");
        if (result.getLinkedin() == null) result.setLinkedin("");
        if (result.getGithub() == null) result.setGithub("");
        if (result.getAboutMe() == null) result.setAboutMe("");

        // Normalize List fields to avoid nulls
        if (result.getSkills() == null) result.setSkills(new ArrayList<>());
        if (result.getExperience() == null) result.setExperience(new ArrayList<>());
        if (result.getEducation() == null) result.setEducation(new ArrayList<>());
        if (result.getProjects() == null) result.setProjects(new ArrayList<>());
        if (result.getCertifications() == null) result.setCertifications(new ArrayList<>());
        if (result.getLanguages() == null) result.setLanguages(new ArrayList<>());
        if (result.getInterests() == null) result.setInterests(new ArrayList<>());
        if (result.getSocialClubs() == null) result.setSocialClubs(new ArrayList<>());

        // Normalize inner Experience objects
        for (CvAnalysisResult.Experience exp : result.getExperience()) {
            if (exp.getCompany() == null) exp.setCompany("");
            if (exp.getRole() == null) exp.setRole("");
            if (exp.getDuration() == null) exp.setDuration("");
            if (exp.getAchievements() == null) exp.setAchievements(new ArrayList<>());
            if (exp.getTechStack() == null) exp.setTechStack(new ArrayList<>());
        }

        // Normalize inner Education objects
        for (CvAnalysisResult.Education edu : result.getEducation()) {
            if (edu.getInstitution() == null) edu.setInstitution("");
            if (edu.getDegree() == null) edu.setDegree("");
            if (edu.getField() == null) edu.setField("");
            if (edu.getStartDate() == null) edu.setStartDate("");
            if (edu.getEndDate() == null) edu.setEndDate("");
        }

        // Normalize inner Project objects
        for (CvAnalysisResult.Project proj : result.getProjects()) {
            if (proj.getTitle() == null) proj.setTitle("");
            if (proj.getDescription() == null) proj.setDescription("");
            if (proj.getTechStack() == null) proj.setTechStack(new ArrayList<>());
            if (proj.getLink() == null) proj.setLink("");
        }

        // Normalize inner Certification objects
        for (CvAnalysisResult.Certification cert : result.getCertifications()) {
            if (cert.getTitle() == null) cert.setTitle("");
            if (cert.getIssuer() == null) cert.setIssuer("");
            if (cert.getYear() == null) cert.setYear("");
        }
    }
}
