package com.example.OllamaAiMicroservice.service;

import com.example.OllamaAiMicroservice.Repositories.CvRepository;
import com.example.OllamaAiMicroservice.entity.*;
import com.example.OllamaAiMicroservice.model.CvAnalysisResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CvStorageService {

    private final CvRepository cvRepository;

    public CvEntity saveCv(CvAnalysisResult result) {
        CvEntity cv = new CvEntity();
        cv.setName(result.getName());
        cv.setEmail(result.getEmail());
        cv.setPhone(result.getPhone());
        cv.setAddress(result.getAddress());
        cv.setPortfolio(result.getPortfolio());
        cv.setGithub(result.getGithub());
        cv.setLinkedin(result.getLinkedin());
        cv.setAboutMe(result.getAboutMe());
        cv.setLanguages(result.getLanguages());
        cv.setInterests(result.getInterests());
        cv.setSocialClubs(result.getSocialClubs());

        // Map Skills
        List<Skill> skills = result.getSkills() != null
                ? result.getSkills().stream().map(skill -> {
            Skill s = new Skill();
            s.setValue(skill);
            s.setCv(cv);
            return s;
        }).collect(Collectors.toList()) : null;
        cv.setSkills(skills);

        // Map Education
        List<Education> educationList = result.getEducation() != null
                ? result.getEducation().stream().map(edu -> {
            Education e = new Education();
            e.setInstitution(edu.getInstitution());
            e.setDegree(edu.getDegree());
            e.setField(edu.getField());
            e.setStartDate(edu.getStartDate());
            e.setEndDate(edu.getEndDate());
            e.setCv(cv);
            return e;
        }).collect(Collectors.toList()) : null;
        cv.setEducation(educationList);

        // Map Experience
        List<Experience> experienceList = result.getExperience() != null
                ? result.getExperience().stream().map(exp -> {
            Experience e = new Experience();
            e.setCompany(exp.getCompany());
            e.setRole(exp.getRole());
            e.setDuration(exp.getDuration());
            e.setAchievements(exp.getAchievements());
            e.setTechStack(exp.getTechStack());
            e.setCv(cv);
            return e;
        }).collect(Collectors.toList()) : null;
        cv.setExperience(experienceList);

        // Map Projects
        List<Project> projects = result.getProjects() != null
                ? result.getProjects().stream().map(p -> {
            Project pr = new Project();
            pr.setTitle(p.getTitle());
            pr.setDescription(p.getDescription());
            pr.setLink(p.getLink());
            pr.setTechStack(p.getTechStack());
            pr.setCv(cv);
            return pr;
        }).collect(Collectors.toList()) : null;
        cv.setProjects(projects);

        // Map Certifications
        List<Certification> certs = result.getCertifications() != null
                ? result.getCertifications().stream().map(c -> {
            Certification cert = new Certification();
            cert.setTitle(c.getTitle());
            cert.setIssuer(c.getIssuer());
            cert.setYear(c.getYear());
            cert.setCv(cv);
            return cert;
        }).collect(Collectors.toList()) : null;
        cv.setCertifications(certs);

        return cvRepository.save(cv);
    }
}
