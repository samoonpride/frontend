package com.samoonpride.line.serviceImpl;

import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.messaging.model.TextMessage;
import com.linecorp.bot.webhook.model.*;
import com.samoonpride.line.dto.MediaDto;
import com.samoonpride.line.dto.UserDto;
import com.samoonpride.line.dto.request.CreateIssueRequest;
import com.samoonpride.line.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import static com.samoonpride.line.config.MessageSourceConfig.getMessage;
import static com.samoonpride.line.enums.MessageCommandEnum.*;
import static com.samoonpride.line.enums.MessageKeys.CREATE_ISSUE_MESSAGE_CANCEL;
import static com.samoonpride.line.messaging.carousel.IssueCarouselBuilder.createIssueCarousel;
import static java.util.Collections.singletonList;

@Log4j2
@AllArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {
    private static final String NO_SUBSCRIBED_ISSUES_MESSAGE = "No subscribed issues.";
    private static final String ISSUE_SUCCESS_MESSAGE = "Issue creation successful.";
    private static final String ERROR_MESSAGE = "An error occurred.";

    private final IssueListServiceImpl issueListService;
    private final VoiceToTextServiceImpl voiceToTextService;
    private final IssueServiceImpl issueService;
    private final SimilarityServiceImpl similarityService;

    @Override
    public List<Message> handleMessage(UserDto userDto, MessageContent message) {
        log.info("Message received from user: {}", userDto.getUserId());
        CreateIssueRequest issueRequest = issueListService.findByUserId(userDto);
        try {
            if (message instanceof TextMessageContent) {
                Optional<List<Message>> optionalTextMessage = Optional.ofNullable(handleTextMessage(issueRequest, (TextMessageContent) message));
                if (optionalTextMessage.isPresent()) {
                    return optionalTextMessage.get();
                }
            } else if (message instanceof AudioMessageContent) {
                handleAudioMessage(issueRequest, (AudioMessageContent) message);
            } else if (message instanceof ImageMessageContent) {
                handleImageMessage(issueRequest, (ImageMessageContent) message);
            } else if (message instanceof VideoMessageContent) {
                handleVideoMessage(issueRequest, (VideoMessageContent) message);
            } else if (message instanceof LocationMessageContent) {
                handleLocationMessage(issueRequest, (LocationMessageContent) message);
            }

            if (issueService.isIssueComplete(issueRequest)) {
                log.info("Issue is complete.");
                // Check for similar issues
                return Optional.ofNullable(similarityService.generateSimilarityIssueMessage(issueRequest))
                        .orElseGet(() -> {
                            issueListService.sendIssue(issueRequest);
                            log.info("Issue creation success.");
                            return singletonList(new TextMessage(ISSUE_SUCCESS_MESSAGE));
                        });
            } else {
                return singletonList(issueService.generateIssueIncompleteMessage(issueRequest));
            }
        } catch (Exception e) {
            log.error("Error occurred: ", e);
            return singletonList(new TextMessage(ERROR_MESSAGE));
        } finally {
            log.info("Current issue: {}", issueRequest);
        }
    }

    private List<Message> handleTextMessage(CreateIssueRequest issue, TextMessageContent textMessage) {
        String text = textMessage.text();
        String command = matchCommand(text);
        if (command == null) {
            List<Double> similarityScores = similarityService.sendSentenceSimilarityCheckerRequest(text, getCommands());
            command = getCommandWithHighestSimilarityScore(similarityScores);
        }
        // If there is a command with a similarity score higher than 0.7, execute the command
        if (command != null) {
            log.info("Command: {}", command);
            return executeCommand(issue, command);
        } else {
            issue.setTitle(text);
        }
        return null;
    }

    private void handleAudioMessage(CreateIssueRequest issue, AudioMessageContent audioMessage) {
        log.info("Audio message received.");
        try {
            String text = voiceToTextService.handleAudioMessage(audioMessage);
            issue.setTitle(text);
        } catch (IOException | ExecutionException | InterruptedException e) {
            log.error("Error handling audio message: ", e);
        }
    }

    private void handleImageMessage(CreateIssueRequest issue, ImageMessageContent imageMessage) {
        log.info("Image message received.");
        MediaDto imageMediaDto = new MediaDto("IMAGE", imageMessage.id());
        issue.getMedia().add(imageMediaDto);
        log.info("Set image media success.");
    }

    private void handleVideoMessage(CreateIssueRequest issue, VideoMessageContent videoMessage) {
        log.info("Video message received.");
        MediaDto videoMediaDto = new MediaDto("VIDEO", videoMessage.id());
        issue.getMedia().add(videoMediaDto);
        log.info("Set video media success.");
    }

    private void handleLocationMessage(CreateIssueRequest issue, LocationMessageContent locationMessage) {
        log.info("Location message received.");
        double latitude = locationMessage.latitude();
        double longitude = locationMessage.longitude();
        log.info("Latitude: {}, Longitude: {}", latitude, longitude);
        issue.setLatitude(latitude);
        issue.setLongitude(longitude);
        log.info("Location acquisition success.");
    }

    private String getCommandWithHighestSimilarityScore(List<Double> similarityScores) {
        // Find the command with the highest similarity score and more than 0.7
        OptionalInt maxIndex = IntStream.range(0, similarityScores.size()).filter(i -> similarityScores.get(i) > 0.7).reduce((i, j) -> similarityScores.get(i) >= similarityScores.get(j) ? i : j);

        // If there is a command with a similarity score higher than 0.7, return the command
        if (maxIndex.isPresent()) {
            return getCommands().get(maxIndex.getAsInt());
        }
        return null;
    }

    private List<Message> executeCommand(CreateIssueRequest issue, String command) {
        String userId = issue.getUser().getUserId();
        if (LATEST_ISSUE.getValue().equals(command)) {
            return singletonList(createIssueCarousel(issueListService.getIssuesByDistinctUser(userId)));
        } else if (SUBSCRIBE_ISSUE.getValue().equals(command)) {
            return singletonList(createIssueCarousel(issueListService.getSubscribedIssues(userId)));
        } else if (MY_ISSUE.getValue().equals(command)) {
            return singletonList(createIssueCarousel(issueListService.getLatestSelfIssues(userId)));
        } else if (CANCEL.getValue().equals(command)) {
            issueListService.removeIssue(issue);
            return singletonList(new TextMessage(getMessage(CREATE_ISSUE_MESSAGE_CANCEL)));
        } else {
            return singletonList(new TextMessage(NO_SUBSCRIBED_ISSUES_MESSAGE));
        }
    }
}
