package com.samoonpride.line.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfig {
    @Getter
    private static String apiBackendUrl;
    @Getter
    private static String apiVoiceToTextUrl;
    @Getter
    private static String apiSimilarityCheckerIssueUrl;
    @Getter
    private static String apiSimilarityCheckerSentenceUrl;

    @Value("${api.backend.url}")
    public void setApiBackendUrl(String url) {
        apiBackendUrl = url;
    }

    @Value("${api.voice.to.text.url}")
    public void setApiVoiceToTextUrl(String url) {
        apiVoiceToTextUrl = url;
    }

    @Value("${api.similarity.checker.issue.url}")
    public void setApiSimilarityCheckerIssueUrl(String url) {
        apiSimilarityCheckerIssueUrl = url;
    }

    @Value("${api.similarity.checker.sentence.url}")
    public void setApiSimilarityCheckerSentenceUrl(String url) {
        apiSimilarityCheckerSentenceUrl = url;
    }
}
