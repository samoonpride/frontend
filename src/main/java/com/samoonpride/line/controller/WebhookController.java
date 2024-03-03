package com.samoonpride.line.controller;

import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.spring.boot.handler.annotation.EventMapping;
import com.linecorp.bot.spring.boot.handler.annotation.LineMessageHandler;
import com.linecorp.bot.webhook.model.MessageEvent;
import com.linecorp.bot.webhook.model.PostbackEvent;
import com.samoonpride.line.dto.UserDto;
import com.samoonpride.line.serviceImpl.LineUserListServiceImpl;
import com.samoonpride.line.serviceImpl.MessageServiceImpl;
import com.samoonpride.line.serviceImpl.PostbackServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@LineMessageHandler
@AllArgsConstructor
public class WebhookController {
    private final LineUserListServiceImpl lineUserListService;
    private final MessageServiceImpl messageService;
    private final PostbackServiceImpl postbackService;

    @EventMapping
    public List<Message> handleMessageEvent(MessageEvent event) {
        UserDto userDto = lineUserListService.findLineUser(event.source().userId());
        return messageService.handleMessage(userDto, event.message());
    }

    @EventMapping
    public Message handlePostbackEvent(PostbackEvent event) {
        UserDto userDto = lineUserListService.findLineUser(event.source().userId());
        return postbackService.handlePostback(userDto, event.postback());
    }
}
