package com.samoonpride.line.serviceImpl;

import com.linecorp.bot.messaging.model.TextMessage;
import com.samoonpride.line.config.ApiConfig;
import com.samoonpride.line.dto.IssueDto;
import com.samoonpride.line.dto.IssueSimilarityBubble;
import com.samoonpride.line.dto.request.SimilarityCheckerRequest;
import com.samoonpride.line.service.SimilarityService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Log4j2
@AllArgsConstructor
public class SimilarityServiceImpl implements SimilarityService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ApiConfig apiConfig;

    public TextMessage generateSimilarityMessage(IssueDto issue) {
        List<IssueSimilarityBubble> issueSimilarityBubbles = sendSimilarityCheckerRequest(issue);
        return new TextMessage(issueSimilarityBubbles.toString());
    }

    private List<IssueSimilarityBubble> sendSimilarityCheckerRequest(IssueDto issue) {
        SimilarityCheckerRequest similarityCheckerRequest = new SimilarityCheckerRequest(
                issue.getTitle(),
                issue.getLatitude(),
                issue.getLongitude()
        );

        // Create HttpHeaders with the appropriate json content type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create RestTemplate and send the file to the Python microservice
        HttpEntity<SimilarityCheckerRequest> requestEntity = new HttpEntity<>(similarityCheckerRequest, headers);
        log.info("Send similarity check request: " + requestEntity);
        List<IssueSimilarityBubble> issueSimilarityBubbles = restTemplate.exchange(
                apiConfig.apiSimilarityCheckerUrl,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<IssueSimilarityBubble>>() {
                }
        ).getBody();
        log.info("Similarity check result: " + issueSimilarityBubbles);
        return issueSimilarityBubbles;
    }
}
