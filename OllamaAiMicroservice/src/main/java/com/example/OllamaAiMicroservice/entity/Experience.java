package com.example.OllamaAiMicroservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String company;
    private String role;
    private String duration;

    @ElementCollection
    private List<String> achievements;

    @ElementCollection
    private List<String> techStack;

    @ManyToOne
    @JsonBackReference
    private CvEntity cv;
}

