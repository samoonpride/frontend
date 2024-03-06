package com.samoonpride.line.service;

import com.samoonpride.line.dto.NotificationBubbleDto;

import java.util.List;

public interface NotificationService {
    void notificationIssue(List<NotificationBubbleDto> notificationBubbleDtoList);
}
