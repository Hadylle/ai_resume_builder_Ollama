package com.example.OllamaAiMicroservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String institution;
    private String degree;
    private String field;
    private String startDate;
    private String endDate;

    @ManyToOne
    @JsonBackReference
    private CvEntity cv;
}
