package com.samoonpride.line.serviceImpl;

import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.messaging.model.TextMessage;
import com.linecorp.bot.webhook.model.*;
import com.samoonpride.line.dto.IssueBubbleDto;
import com.samoonpride.line.dto.MediaDto;
import com.samoonpride.line.dto.UserDto;
import com.samoonpride.line.dto.request.CreateIssueRequest;
import com.samoonpride.line.messaging.carousel.IssueCarouselBuilder;
import com.samoonpride.line.service.MessageService;
import com.samoonpride.line.utils.ThumbnailUtils;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

@Log4j2
@AllArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {
    private static final String ISSUE_SUCCESS_MESSAGE = "Issue creation successful.";
    private static final String ERROR_MESSAGE = "An error occurred.";
    private static final String[] COMMANDS = {"Latest Issue", "Subscribe Issue", "My Issue"};

    private final IssueListServiceImpl issueListService;
    private final VoiceToTextServiceImpl voiceToTextService;
    private final ImageServiceImpl imageService;
    private final VideoServiceImpl videoService;
    private final IssueServiceImpl issueService;
    private final SimilarityServiceImpl similarityService;

    @Override
    public List<Message> handleMessage(UserDto userDto, MessageContent message) {
        log.info("Message received from user: {}", userDto.getUserId());
        CreateIssueRequest issueRequest = issueListService.findByUserId(userDto);
        try {
            if (message instanceof TextMessageContent) {
                Optional<Message> optionalTextMessage = Optional.ofNullable(handleTextMessage(issueRequest, (TextMessageContent) message));
                if (optionalTextMessage.isPresent()) {
                    return (List<Message>) optionalTextMessage.get();
                }
            } else if (message instanceof AudioMessageContent) {
                handleAudioMessage(issueRequest, (AudioMessageContent) message);
            } else if (message instanceof ImageMessageContent) {
                handleImageMessage(issueRequest, userDto.getUserId(), (ImageMessageContent) message);
            } else if (message instanceof VideoMessageContent) {
                handleVideoMessage(issueRequest, userDto.getUserId(), (VideoMessageContent) message);
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
                            return Collections.singletonList(new TextMessage(ISSUE_SUCCESS_MESSAGE));
                        });
            } else {
                return Collections.singletonList(issueService.generateIssueIncompleteMessage(issueRequest));
            }
        } catch (Exception e) {
            log.error("Error occurred: ", e);
            return Collections.singletonList(new TextMessage(ERROR_MESSAGE));
        } finally {
            log.info("Current issue: {}", issueRequest);
        }
    }

    private Message handleTextMessage(CreateIssueRequest issue, TextMessageContent textMessage) {
        String text = textMessage.text();
        List<Double> similarityScores = similarityService.sendSentenceSimilarityCheckerRequest(text, COMMANDS);
        String command = getCommandWithHighestSimilarityScore(similarityScores);
        // If there is a command with a similarity score higher than 0.7, execute the command
        if (command != null) {
            log.info("Command: {}", command);
            List<IssueBubbleDto> issueBubbleDtoList = executeCommand(issue, command);
            assert issueBubbleDtoList != null;
            return IssueCarouselBuilder.createIssueCarousel(issueBubbleDtoList);
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

    private void handleImageMessage(CreateIssueRequest issue, String userId, ImageMessageContent imageMessage) {
        log.info("Image message received.");
        try {
            Path imagePath = imageService.createImage(userId, imageMessage.id());
            MediaDto imageMediaDto = imageService.createImageMediaDto(imagePath.toString(), imageMessage.id());
            // Add public to the path
            Path publicPath = Path.of("public").resolve(imagePath);
            String thumbnailPath = ThumbnailUtils.createThumbnail(publicPath.toFile());
            issue.getMedia().add(imageMediaDto);
            issue.setThumbnailPath(thumbnailPath);
            log.info("Image creation success.");
        } catch (IOException | ExecutionException | InterruptedException e) {
            log.error("Error handling image message: ", e);
        }
    }

    private void handleVideoMessage(CreateIssueRequest issue, String userId, VideoMessageContent videoMessage) {
        log.info("Video message received.");
        try {
            Path videoPath = videoService.createVideo(userId, videoMessage.id());
            MediaDto videoMediaDto = videoService.createVideoMediaDto(videoPath.toString(), videoMessage.id());
            Path publicPath = Path.of("public").resolve(videoPath);
            String thumbnailPath = ThumbnailUtils.createThumbnail(publicPath.toFile());
            issue.getMedia().add(videoMediaDto);
            issue.setThumbnailPath(thumbnailPath);
            log.info("Video creation success.");
        } catch (IOException | ExecutionException | InterruptedException e) {
            log.error("Error handling video message: ", e);
        }
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
            return COMMANDS[maxIndex.getAsInt()];
        }
        return null;
    }

    private List<IssueBubbleDto> executeCommand(CreateIssueRequest issue, String command) {
        String userId = issue.getUser().getUserId();
        if (command.equals(COMMANDS[0])) {
            return issueListService.getIssuesByDistinctUser(userId);
        } else if (command.equals(COMMANDS[1])) {
            return issueListService.getSubscribedIssues(userId);
        } else if (command.equals(COMMANDS[2])) {
            return issueListService.getLatestSelfIssues(userId);
        } else {
            return new ArrayList<>();
        }
    }
}
