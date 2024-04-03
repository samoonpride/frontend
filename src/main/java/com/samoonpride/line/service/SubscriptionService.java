package com.samoonpride.line.service;

public interface SubscriptionService {
    void subscribe(String lineUserId, String issueId);

    void unsubscribe(String lineUserId, String issueId);
}
