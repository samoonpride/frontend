package com.samoonpride.line.serviceImpl;

import com.samoonpride.line.config.ApiConfig;
import com.samoonpride.line.config.WebClientConfig;
import com.samoonpride.line.dto.request.SubscribeRequest;
import com.samoonpride.line.service.SubscriptionService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final WebClientConfig webClientConfig;
    @Override
    public void subscribe(String lineUserId, String issueId) {
        log.info("Subscribe issueId: " + issueId + " to lineUserId: " + lineUserId);
        SubscribeRequest request = new SubscribeRequest(lineUserId, issueId);
        webClientConfig.webClient()
                .post()
                .uri(ApiConfig.getApiBackendUrl() + "/subscription/line-user/subscribe")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();
        log.info("Subscribe issueId: " + issueId + " to lineUserId: " + lineUserId + " success");
    }

    @Override
    public void unsubscribe(String lineUserId, String issueId) {
        log.info("Unsubscribe issueId: " + issueId + " to lineUserId: " + lineUserId);
        SubscribeRequest request = new SubscribeRequest(lineUserId, issueId);
        webClientConfig.webClient()
                .delete()
                .uri(ApiConfig.getApiBackendUrl() + "/subscription/line-user/{lineUserId}/unsubscribe/{issueId}", lineUserId, issueId)
                .retrieve()
                .bodyToMono(String.class)
                .subscribe();
        log.info("Unsubscribe issueId: " + issueId + " to lineUserId: " + lineUserId + " success");
    }
}