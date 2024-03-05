package com.samoonpride.line.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Getter
    private static String appUrl;
    @Getter
    private static String backendUrl;

    @Value("${app.url}")
    public void setAppUrl(String url) {
        appUrl = url;
    }
    @Value("${backend.url}")
    public void setBackendUrl(String url) {
        backendUrl = url;
    }
}
