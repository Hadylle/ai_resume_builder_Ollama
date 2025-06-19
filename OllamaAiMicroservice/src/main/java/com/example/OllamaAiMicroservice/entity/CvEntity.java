package com.example.OllamaAiMicroservice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CvEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 255)

    private String name;
    @Column(length = 255)

    private String email;
    @Column(length = 255)

    private String phone;
    @Column(length = 255)

    private String address;
    @Column(length = 255)

    private String portfolio;
    @Column(length = 255)

    private String linkedin;
    @Column(length = 255)

    private String github;
    @Column(columnDefinition = "TEXT")

    private String aboutMe;

    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Skill> skills;

    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Education> education;

    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Experience> experience;

    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Project> projects;

    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Certification> certifications;

    @ElementCollection
    private List<String> languages;

    @ElementCollection
    private List<String> interests;

    @ElementCollection
    private List<String> socialClubs;
}
