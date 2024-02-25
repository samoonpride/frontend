package com.samoonpride.line.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samoonpride.line.config.ApiConfig;
import com.samoonpride.line.config.WebClientConfig;
import com.samoonpride.line.dto.IssueBubbleDto;
import com.samoonpride.line.dto.request.CreateIssueRequest;
import com.samoonpride.line.dto.UserDto;
import com.samoonpride.line.service.IssueListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class IssueListServiceImpl implements IssueListService {
    private final List<CreateIssueRequest> createIssueRequestList = new ArrayList<>();
    private final RestTemplate restTemplate = new RestTemplate();
    private final ApiConfig apiConfig;
    private final WebClientConfig webClientConfig;
    private final ObjectMapper objectMapper;

    @Override
    public void addIssue(CreateIssueRequest createIssueRequest) {
        if (createIssueRequestList.contains(createIssueRequest)) {
            return;
        }
        createIssueRequestList.add(createIssueRequest);
    }

    @Override
    public CreateIssueRequest findByUserId(UserDto userDto) {
        return createIssueRequestList.stream()
                .filter(issueDto -> issueDto.getUser().getUserId().equals(userDto.getUserId()))
                .findFirst()
                .orElseGet(() -> {
                    CreateIssueRequest createIssueRequest = new CreateIssueRequest(userDto);
                    this.addIssue(createIssueRequest);
                    return createIssueRequest;
                });
    }

    // send issue to backend and remove from list
    @Override
    public void sendIssue(CreateIssueRequest createIssueRequest) {
        URI uri = URI.create(apiConfig.apiBackendUrl + "/issue/create");
        String response = webClientConfig.webClient()
                .post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createIssueRequest)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("Create issue success");
        log.info("Response: " + response);
        createIssueRequestList.remove(createIssueRequest);
    }

    @Override
    public List<IssueBubbleDto> getLatestIssues(String userId) {
        URI uri = URI.create(apiConfig.apiBackendUrl + "/issue/line-user/get/" + userId + "/latest");
        List<IssueBubbleDto> issueBubbleDtoList = webClientConfig.webClient()
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<IssueBubbleDto>>() {
                })
                .block();
        log.info("Latest Issue");
        log.info("IssueBubbleDtoList: " + issueBubbleDtoList);
        return issueBubbleDtoList;
    }

    @Override
    public List<IssueBubbleDto> getIssuesByDistinctUser(String userId) {
        URI uri = URI.create(apiConfig.apiBackendUrl + "/issue/line-user/get/" + userId + "/distinct");
        List<IssueBubbleDto> issueBubbleDtoList = webClientConfig.webClient()
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<IssueBubbleDto>>() {
                })
                .block();
        log.info("Distinct Issue");
        log.info("IssueBubbleDtoList: " + issueBubbleDtoList);
        return issueBubbleDtoList;
    }

    @Override
    public List<IssueBubbleDto> getSubscribedIssues(String userId) {
        URI uri = URI.create(apiConfig.apiBackendUrl + "/issue/line-user/get/" + userId + "/subscribed");
        List<IssueBubbleDto> issueBubbleDtoList = webClientConfig.webClient()
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<IssueBubbleDto>>() {
                })
                .block();
        log.info("Subscribed Issue");
        log.info("IssueBubbleDtoList: " + issueBubbleDtoList);
        return issueBubbleDtoList;
    }
}
