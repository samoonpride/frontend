package com.samoonpride.line.serviceImpl;

import com.samoonpride.line.config.ApiConfig;
import com.samoonpride.line.config.WebClientConfig;
import com.samoonpride.line.dto.request.SubscribeRequest;
import com.samoonpride.line.service.SubscribeService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class SubscribeServiceImpl implements SubscribeService{
    private final ApiConfig apiConfig;
    private final WebClientConfig webClientConfig;
    @Override
    public void subscribe(String lineUserId, String issueId) {
        log.info("Subscribe issueId: " + issueId + " to lineUserId: " + lineUserId);
        SubscribeRequest request = new SubscribeRequest(lineUserId, issueId);
        webClientConfig.webClient()
                .post()
                .uri(apiConfig.apiBackendUrl + "/subscribe/line-user/subscribe")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();
        log.info("Subscribe issueId: " + issueId + " to lineUserId: " + lineUserId + " success");
    }
}
