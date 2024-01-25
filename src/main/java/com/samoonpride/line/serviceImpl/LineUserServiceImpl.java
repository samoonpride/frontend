package com.samoonpride.line.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.messaging.client.MessagingApiClient;
import com.samoonpride.line.config.ApiConfig;
import com.samoonpride.line.dto.UserDto;
import com.samoonpride.line.dto.request.CreateLineUserRequest;
import com.samoonpride.line.service.LineUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Log4j2
@RequiredArgsConstructor
@Service
public class LineUserServiceImpl implements LineUserService {
    private final MessagingApiClient messagingApiClient;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ApiConfig apiConfig;

    @Override
    public UserDto createLineUser(String userId) {
        messagingApiClient.getProfile(userId).whenCompleteAsync((profile, throwable) -> {
            CreateLineUserRequest request = new CreateLineUserRequest(
                    userId,
                    profile.body().displayName()
            );
            try {
                String jsonBody = new ObjectMapper().writeValueAsString(request);
                HttpHeaders jsonHeaders = new HttpHeaders();
                jsonHeaders.add("Content-Type", "application/json");
                // send to backend
                HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, jsonHeaders);

                log.info("Request: " + jsonBody);
                // Send the POST request and get the response
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                        apiConfig.apiBackendUrl + "/line-user/create",
                        requestEntity,
                        String.class
                );

                // Extract and print the response
                String responseBody = responseEntity.getStatusCode() + " " + responseEntity.getBody();
                log.info("Response: " + responseBody);
            } catch (JsonProcessingException e) {
                System.out.printf("Error: %s\n", e.getMessage());
            }
        });
        return createUserDto(userId);
    }

    private UserDto createUserDto(String userId) {
        return UserDto.builder()
                .type("LINE")
                .key(userId)
                .build();
    }
}
