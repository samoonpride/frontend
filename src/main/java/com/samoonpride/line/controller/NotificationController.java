package com.samoonpride.line.controller;

import com.samoonpride.line.dto.NotificationBubbleDto;
import com.samoonpride.line.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notify")
@AllArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/issue")
    public void notifyIssue(List<NotificationBubbleDto> notificationBubbleDtoList) {
        notificationService.notificationIssue(notificationBubbleDtoList);
    }
}
