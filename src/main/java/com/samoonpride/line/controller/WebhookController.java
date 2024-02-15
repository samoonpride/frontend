package com.samoonpride.line.controller;

import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.spring.boot.handler.annotation.EventMapping;
import com.linecorp.bot.spring.boot.handler.annotation.LineMessageHandler;
import com.linecorp.bot.webhook.model.MessageContent;
import com.linecorp.bot.webhook.model.MessageEvent;
import com.samoonpride.line.dto.UserDto;
import com.samoonpride.line.serviceImpl.IssueServiceImpl;
import com.samoonpride.line.serviceImpl.LineUserListServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Log4j2
@RestController
@LineMessageHandler
@AllArgsConstructor
public class WebhookController {
    private final LineUserListServiceImpl lineUserListService;
    private final IssueServiceImpl issueService;

    @EventMapping
    public Message handleMessageEvent(MessageEvent event) {
        try {
            UserDto userDto = lineUserListService.findLineUser(event.source().userId());
            return issueService.createIssue(userDto, event.message());
        } catch (IOException | ExecutionException | InterruptedException e) {
            return null;
        }
    }
}
