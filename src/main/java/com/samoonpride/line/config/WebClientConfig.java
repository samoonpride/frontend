package com.samoonpride.line.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@AllArgsConstructor
@Configuration
public class WebClientConfig {
    private final WebClient.Builder webClientBuilder;

    @Bean
    public WebClient webClient() {
        return webClientBuilder.build();
    }
}
