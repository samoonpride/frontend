package com.samoonpride.line.controller;

import com.samoonpride.line.dto.NotificationBubbleDto;
import com.samoonpride.line.utils.NotificationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/notify")
@AllArgsConstructor
public class NotificationController {
    @PostMapping("/issue")
    public void notifyIssue(@RequestBody List<NotificationBubbleDto> notificationBubbleDtoList) {
        log.info("notifyIssue: {}", notificationBubbleDtoList);
        NotificationUtils.notificationUpdateStatusIssue(notificationBubbleDtoList);
    }
}
