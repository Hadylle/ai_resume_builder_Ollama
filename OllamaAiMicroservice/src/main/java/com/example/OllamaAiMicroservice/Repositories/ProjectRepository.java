package com.example.OllamaAiMicroservice.Repositories;

import com.example.OllamaAiMicroservice.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjectRepository extends JpaRepository<Project, Long> {}
