package com.samoonpride.line.serviceImpl;

import com.linecorp.bot.messaging.client.MessagingApiClient;
import com.samoonpride.line.config.ApiConfig;
import com.samoonpride.line.config.WebClientConfig;
import com.samoonpride.line.dto.UserDto;
import com.samoonpride.line.dto.request.CreateLineUserRequest;
import com.samoonpride.line.service.LineUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Log4j2
@RequiredArgsConstructor
@Service
public class LineUserServiceImpl implements LineUserService {
    private final MessagingApiClient messagingApiClient;
    private final RestTemplate restTemplate = new RestTemplate();
    private final WebClientConfig webClientConfig;
    private final ApiConfig apiConfig;

    @Override
    public UserDto createLineUser(String userId) {
        messagingApiClient.getProfile(userId).whenCompleteAsync((profile, throwable) -> {
            CreateLineUserRequest request = new CreateLineUserRequest(
                    userId,
                    profile.body().displayName()
            );
            webClientConfig.webClient()
                    .post()
                    .uri(apiConfig.apiBackendUrl + "/line-user/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(response -> log.info("Response: " + response));

        });
        return createUserDto(userId);
    }

    private UserDto createUserDto(String userId) {
        return UserDto.builder()
                .type("LINE")
                .userId(userId)
                .build();
    }
}
