package com.samoonpride.line.serviceImpl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samoonpride.line.config.ApiConfig;
import com.samoonpride.line.dto.ReportDto;
import com.samoonpride.line.dto.UserDto;
import com.samoonpride.line.service.ReportListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReportListServiceImpl implements ReportListService {
    private final List<ReportDto> reportDtoList = new ArrayList<>();
    private final RestTemplate restTemplate = new RestTemplate();
    private final ApiConfig apiConfig;

    @Override
    public void addReport(ReportDto reportDto) {
        if (reportDtoList.contains(reportDto)) {
            return;
        }
        reportDtoList.add(reportDto);
    }

    @Override
    public ReportDto findByUserId(UserDto userDto) {
        return reportDtoList.stream()
                .filter(reportDto -> reportDto.getUser().getKey().equals(userDto.getKey()))
                .findFirst()
                .orElseGet(() -> {
                    ReportDto reportDto = new ReportDto(userDto);
                    this.addReport(reportDto);
                    return reportDto;
                });
    }

    // send report to backend and remove from list
    @Override
    public void sendReport(ReportDto reportDto) {
        try {
            String jsonBody = new ObjectMapper().writeValueAsString(reportDto);
            HttpHeaders jsonHeaders = new HttpHeaders();
            jsonHeaders.add("Content-Type", "application/json");

            // send to backend
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, jsonHeaders);

            log.info("Request: " + jsonBody);
            // Send the POST request and get the response
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    apiConfig.apiBackendUrl + "/report/create",
                    requestEntity,
                    String.class
            );

            // Extract and print the response
            String responseBody = responseEntity.getStatusCode() + " " + responseEntity.getBody();
            log.info("Response: " + responseBody);
        } catch (JsonProcessingException e) {
            System.out.printf("Error: %s\n", e.getMessage());
        }
        reportDtoList.remove(reportDto);
    }
}
