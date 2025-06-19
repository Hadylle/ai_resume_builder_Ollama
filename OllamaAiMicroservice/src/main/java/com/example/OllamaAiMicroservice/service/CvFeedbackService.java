package com.example.OllamaAiMicroservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CvFeedbackService {

    private final OllamaChatModel chatModel;

    public String generateFeedback(String cvText) {
        String prompt = """
You are an expert career coach and resume analyst.

Your task is to **analyze the following resume text** (between the --- lines) and provide a clear evaluation of its strengths and areas for improvement.

---
%s
---

Respond with a structured and readable analysis in **plain text** using the following format:

CV Review:
==========

✅ **Strengths**
- ...

⚠️ **Areas for Improvement**
- ...

🚫 **Missing or Weak Sections**
- ...

📊 **Overall Impression**
Provide a short paragraph summarizing your evaluation of this CV. You can optionally give a score out of 10 if appropriate.

Your feedback should be helpful, actionable, and professional. Avoid generic or vague suggestions.
""".formatted(cvText);


        OllamaOptions options = new OllamaOptions();
        options.setModel("llama3");
        options.setTemperature(0.9);

        ChatResponse response = chatModel.call(new Prompt(prompt, options));
        return response.getResult().getOutput().getText();
    }
}
