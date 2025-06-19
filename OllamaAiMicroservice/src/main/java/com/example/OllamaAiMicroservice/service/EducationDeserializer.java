package com.example.OllamaAiMicroservice.service;

import com.example.OllamaAiMicroservice.model.CvAnalysisResult;
import com.example.OllamaAiMicroservice.model.CvAnalysisResult.Education;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class EducationDeserializer extends StdDeserializer<Education> {

    public EducationDeserializer() {
        super(CvAnalysisResult.Education.class);
    }

    @Override
    public CvAnalysisResult.Education deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        CvAnalysisResult.Education education = new CvAnalysisResult.Education();

        JsonNode institutionNode = node.get("institution");
        education.setInstitution(institutionNode != null ? institutionNode.asText() : null);

        JsonNode degreeNode = node.get("degree");
        education.setDegree(degreeNode != null ? degreeNode.asText() : null);

        JsonNode fieldNode = node.get("field");
        education.setField(fieldNode != null ? fieldNode.asText() : null);

        JsonNode startDateNode = node.get("startDate");
        education.setStartDate(startDateNode != null ? startDateNode.asText() : null);

        JsonNode endDateNode = node.get("endDate");
        education.setEndDate(endDateNode != null ? endDateNode.asText() : null);

        return education;
    }

}
