package com.samoonpride.line.controller;

import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.spring.boot.handler.annotation.EventMapping;
import com.linecorp.bot.spring.boot.handler.annotation.LineMessageHandler;
import com.linecorp.bot.webhook.model.*;
import com.samoonpride.line.dto.MediaDto;
import com.samoonpride.line.dto.IssueDto;
import com.samoonpride.line.dto.UserDto;
import com.samoonpride.line.serviceImpl.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Log4j2
@RestController
@LineMessageHandler
@AllArgsConstructor
public class WebhookController {
    private final VoiceToTextServiceImpl voiceToTextService;
    private final ImageServiceImpl imageService;
    private final VideoServiceImpl videoService;
    private final IssueListServiceImpl issueListService;
    private final LineUserListServiceImpl lineUserListService;

    @EventMapping
    public Message handleMessageEvent(MessageEvent event) throws IOException, ExecutionException, InterruptedException {
        return checkMessage(event.source().userId(), event.message());
    }

    private Message checkMessage(String userId, MessageContent message) throws IOException, ExecutionException, InterruptedException {
        log.info("Got message from user: " + userId);
        UserDto userDto = lineUserListService.findLineUser(userId);

        IssueDto issue = issueListService.findByUserId(userDto);
        if (message instanceof TextMessageContent) {
            String text = ((TextMessageContent) message).text();
            if (Objects.equals(text, "สร้างรายงาน")) {
                issueListService.sendIssue(issue);
                log.info("Create issue success");
            } else {
                issue.setTitle(((TextMessageContent) message).text());
            }
        } else if (message instanceof AudioMessageContent) {
            log.info("Got audio message");
            return voiceToTextService.handleAudioMessage((AudioMessageContent) message);
        } else if (message instanceof ImageMessageContent) {
            log.info("Got image message");
            MediaDto mediaDto = imageService.createImage(userId, message.id());
            issue.getMedia().add(mediaDto);
            log.info("Create image success");
        } else if (message instanceof VideoMessageContent) {
            log.info("Got video message");
            videoService.createVideo(userId, message.id());
            log.info("Create video success");
        } else if (message instanceof LocationMessageContent) {
            log.info("Got location message");
            String latitude = String.valueOf(((LocationMessageContent) message).latitude());
            String longitude = String.valueOf(((LocationMessageContent) message).longitude());
            log.info("Got latitude: " + latitude + " and longitude: " + longitude);
            issue.setLatitude(((LocationMessageContent) message).latitude());
            issue.setLongitude(((LocationMessageContent) message).longitude());
            log.info("Get location success");
        }
        log.info("Issue: " + issue.toString());
        return null;
    }
}
