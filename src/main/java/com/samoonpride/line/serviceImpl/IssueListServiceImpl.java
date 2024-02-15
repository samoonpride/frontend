package com.samoonpride.line.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samoonpride.line.config.ApiConfig;
import com.samoonpride.line.config.WebClientConfig;
import com.samoonpride.line.dto.IssueBubbleDto;
import com.samoonpride.line.dto.IssueDto;
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
    private final List<IssueDto> issueDtoList = new ArrayList<>();
    private final RestTemplate restTemplate = new RestTemplate();
    private final ApiConfig apiConfig;
    private final WebClientConfig webClientConfig;
    private final ObjectMapper objectMapper;

    @Override
    public void addIssue(IssueDto issueDto) {
        if (issueDtoList.contains(issueDto)) {
            return;
        }
        issueDtoList.add(issueDto);
    }

    @Override
    public IssueDto findByUserId(UserDto userDto) {
        return issueDtoList.stream()
                .filter(issueDto -> issueDto.getUser().getUserId().equals(userDto.getUserId()))
                .findFirst()
                .orElseGet(() -> {
                    IssueDto issueDto = new IssueDto(userDto);
                    this.addIssue(issueDto);
                    return issueDto;
                });
    }

    // send issue to backend and remove from list
    @Override
    public void sendIssue(IssueDto issueDto) {
        URI uri = URI.create(apiConfig.apiBackendUrl + "/issue/create");
        String response = webClientConfig.webClient()
                .post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(issueDto)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("Response: " + response);
        issueDtoList.remove(issueDto);
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
        log.info("IssueBubbleDtoList: " + issueBubbleDtoList);
        return issueBubbleDtoList;
    }
}
