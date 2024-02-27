package com.samoonpride.line.service;

public interface SubscribeService {
    void subscribe(String lineUserId, String issueId);

    void unsubscribe(String lineUserId, String issueId);
}
