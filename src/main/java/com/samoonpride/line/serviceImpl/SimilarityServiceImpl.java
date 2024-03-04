package com.samoonpride.line.serviceImpl;

import com.linecorp.bot.messaging.model.Message;
import com.samoonpride.line.config.ApiConfig;
import com.samoonpride.line.dto.SimilarityBubbleDto;
import com.samoonpride.line.dto.request.CreateIssueRequest;
import com.samoonpride.line.dto.request.IssueSimilarityCheckerRequest;
import com.samoonpride.line.dto.request.SentenceSimilarityCheckerRequest;
import com.samoonpride.line.messaging.QuickReplyBuilder;
import com.samoonpride.line.messaging.carousel.DuplicateIssueCarouselBuilder;
import com.samoonpride.line.service.SimilarityService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@Log4j2
@AllArgsConstructor
public class SimilarityServiceImpl implements SimilarityService {
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Message> generateSimilarityIssueMessage(CreateIssueRequest issue) {
        List<SimilarityBubbleDto> similarityBubbleDtoList = sendIssueSimilarityCheckerRequest(issue);
        if (similarityBubbleDtoList.isEmpty()) {
            return null;
        }
        return Arrays.asList(
                DuplicateIssueCarouselBuilder.createIssueCarousel(similarityBubbleDtoList),
                QuickReplyBuilder.createNoDuplicateQuickReplyMessage()
        );
    }

    private List<SimilarityBubbleDto> sendIssueSimilarityCheckerRequest(CreateIssueRequest issue) {
        IssueSimilarityCheckerRequest issueSimilarityCheckerRequest = new IssueSimilarityCheckerRequest(
                issue.getTitle(),
                issue.getLatitude(),
                issue.getLongitude()
        );

        // Create HttpHeaders with the appropriate json content type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create RestTemplate and send the file to the Python microservice
        HttpEntity<IssueSimilarityCheckerRequest> requestEntity = new HttpEntity<>(issueSimilarityCheckerRequest, headers);
        log.info("Send similarity check request: " + requestEntity);
        ResponseEntity<List<SimilarityBubbleDto>> response = restTemplate.exchange(
                ApiConfig.getApiSimilarityCheckerIssueUrl(),
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        // If the response is 204, there are no similar issues
        if (response.getStatusCode() == HttpStatus.resolve(204)) {
            log.info("No similar issues found");
            return List.of();
        }

        List<SimilarityBubbleDto> similarityBubbleDtos = response.getBody();
        log.info("Similarity check result: " + similarityBubbleDtos);
        return similarityBubbleDtos;
    }

    public List<Double> sendSentenceSimilarityCheckerRequest(String sourceSentence, String[] sentences) {
        SentenceSimilarityCheckerRequest sentenceRequest = new SentenceSimilarityCheckerRequest(
                sourceSentence,
                Arrays.asList(sentences)
        );


        // Create HttpHeaders with the appropriate json content type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create RestTemplate and send the file to the Python microservice
        HttpEntity<SentenceSimilarityCheckerRequest> requestEntity = new HttpEntity<>(sentenceRequest, headers);
        log.info("Send similarity check request: " + requestEntity);
        ResponseEntity<List<Double>> response = restTemplate.exchange(
                ApiConfig.getApiSimilarityCheckerSentenceUrl(),
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );
        List<Double> sentenceSimilarity = response.getBody();
        log.info("Similarity check result: " + sentenceSimilarity);
        return sentenceSimilarity;
    }


}
