package com.samoonpride.line.serviceImpl;

import com.linecorp.bot.messaging.model.TextMessage;
import com.linecorp.bot.webhook.model.*;
import com.samoonpride.line.dto.IssueDto;
import com.samoonpride.line.dto.MediaDto;
import com.samoonpride.line.dto.UserDto;
import com.samoonpride.line.service.MessageService;
import com.samoonpride.line.utils.ThumbnailUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Log4j2
@AllArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {
    private static final String ISSUE_SUCCESS_MESSAGE = "เสร็จสิ้นการสร้างรายงาน";
    private static final String ERROR_MESSAGE = "เกิดข้อผิดพลาด";

    private final IssueListServiceImpl issueListService;
    private final VoiceToTextServiceImpl voiceToTextService;
    private final ImageServiceImpl imageService;
    private final VideoServiceImpl videoService;
    private final IssueServiceImpl issueService;

    @Override
    public TextMessage handleMessage(UserDto userDto, MessageContent message) throws IOException, ExecutionException, InterruptedException {
        log.info("Got message from user: " + userDto.getUserId());
        IssueDto issue = issueListService.findByUserId(userDto);
        try {
            if (message instanceof TextMessageContent) {
                TextMessage textMessage = handleTextMessage(issue, (TextMessageContent) message);
                if (textMessage != null) {
                    return textMessage;
                }
            } else if (message instanceof AudioMessageContent) {
                handleAudioMessage(issue, (AudioMessageContent) message);
            } else if (message instanceof ImageMessageContent) {
                handleImageMessage(issue, userDto.getUserId(), (ImageMessageContent) message);
            } else if (message instanceof VideoMessageContent) {
                handleVideoMessage(issue, userDto.getUserId(), (VideoMessageContent) message);
            } else if (message instanceof LocationMessageContent) {
                handleLocationMessage(issue, (LocationMessageContent) message);
            }

            if (issueService.isIssueComplete(issue)) {
                issueListService.sendIssue(issue);
                log.info("Create issue success");
                return new TextMessage(ISSUE_SUCCESS_MESSAGE);
            } else {
                return issueService.checkIssueIncomplete(issue);
            }

        } catch (Exception e) {
            log.error("Error: ", e);
            return new TextMessage(ERROR_MESSAGE);
        } finally {
            log.info("Issue : " + issue);
        }
    }

    private TextMessage handleTextMessage(IssueDto issue, TextMessageContent textMessage) {
        String text = textMessage.text();
        if (Objects.equals(text, "ปัญหาล่าสุด")) {
            return new TextMessage(issueListService.getLatestIssues(issue.getUser().getUserId()).toString());
        } else {
            issue.setTitle(text);
        }
        return null;
    }

    private void handleAudioMessage(IssueDto issue, AudioMessageContent audioMessage) throws IOException, ExecutionException, InterruptedException {
        log.info("Got audio message");
        String text = voiceToTextService.handleAudioMessage(audioMessage);
        issue.setTitle(text);
    }

    private void handleImageMessage(IssueDto issue, String userId, ImageMessageContent imageMessage) throws IOException, ExecutionException, InterruptedException {
        log.info("Got image message");

        Path imagePath = imageService.createImage(userId, imageMessage.id());
        MediaDto imageMediaDto = imageService.createImageMediaDto(imagePath.toString(), imageMessage.id());
        String thumbnailPath = ThumbnailUtils.createThumbnail(imagePath.toFile());

        issue.getMedia().add(imageMediaDto);
        issue.setThumbnailPath(thumbnailPath);

        log.info("Create image success");
    }

    private void handleVideoMessage(IssueDto issue, String userId, VideoMessageContent videoMessage) throws IOException, ExecutionException, InterruptedException {
        log.info("Got video message");

        Path videoPath = videoService.createVideo(userId, videoMessage.id());
        MediaDto videoMediaDto = videoService.createVideoMediaDto(videoPath.toString(), videoMessage.id());
        String thumbnailPath = ThumbnailUtils.createThumbnail(videoPath.toFile());

        issue.getMedia().add(videoMediaDto);
        issue.setThumbnailPath(thumbnailPath);

        log.info("Create video success");
    }

    private void handleLocationMessage(IssueDto issue, LocationMessageContent locationMessage) {
        log.info("Got location message");
        double latitude = locationMessage.latitude();
        double longitude = locationMessage.longitude();
        log.info("Got latitude: " + latitude + " and longitude: " + longitude);
        issue.setLatitude(latitude);
        issue.setLongitude(longitude);
        log.info("Get location success");
    }
}
