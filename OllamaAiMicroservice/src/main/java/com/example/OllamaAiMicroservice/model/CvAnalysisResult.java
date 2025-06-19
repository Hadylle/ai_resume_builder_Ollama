package com.example.OllamaAiMicroservice.model;


import com.example.OllamaAiMicroservice.service.EducationDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import java.util.List;

@Data
public class CvAnalysisResult {
    private String name;
    private String email;
    private String phone;
    private String address;
    private String portfolio;
    private String linkedin;
    private String github;
    private String aboutMe;
    private List<String> skills;
    private List<Experience> experience;
    private List<Education> education;
    private List<Project> projects;
    private List<Certification> certifications;
    private List<String> languages;
    private List<String> interests;
    private List<String> socialClubs;

    @Data
    public static class Experience {
        private String company;
        private String role;
        private String duration;
        private List<String> achievements;
        private List<String> techStack;
    }
    @Data
    @JsonDeserialize(using = EducationDeserializer.class)

    public static class Education {
        private String institution;
        private String degree;
        private String field;
        private String startDate;
        private String endDate;
    }
    @Data
    public static class Project {
        private String title;
        private String description;
        private List<String> techStack;
        private String link; // GitHub or deployed link
    }

    @Data
    public static class Certification {
        private String title;
        private String issuer;
        private String year;
    }
}
