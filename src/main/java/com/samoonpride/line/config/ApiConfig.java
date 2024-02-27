package com.samoonpride.line.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {
    @Value("${api.backend.url}")
    public String apiBackendUrl;

    @Value("${api.voice.to.text.url}")
    public String apiVoiceToTextUrl;

    @Value("${api.similarity.checker.issues.url}")
    public String apiSimilarityCheckerIssuesUrl;

    @Value("${api.similarity.checker.sentence.url}")
    public String apiSimilarityCheckerSentenceUrl;
}
