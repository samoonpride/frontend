package com.samoonpride.line.serviceImpl;

import com.linecorp.bot.client.base.BlobContent;
import com.linecorp.bot.messaging.client.MessagingApiBlobClient;
import com.samoonpride.line.dto.MediaDto;
import com.samoonpride.line.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Log4j2
@RequiredArgsConstructor
@Service
public class VideoServiceImpl implements VideoService {
    private final MessagingApiBlobClient lineMessagingClient;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public Path createVideo(String userId, String eventId) throws ExecutionException, InterruptedException, IOException {
        log.info("Got event id: " + eventId);
        return saveVideoToStorage(userId, lineMessagingClient.getMessageContent(eventId).get().body());
    }

    private Path saveVideoToStorage(String userId, BlobContent blobContent) throws IOException {
        Path videoPath = Path.of("public/videos/" + LocalDateTime.now().format(dateTimeFormatter) + "/" + userId);
        String extension = blobContent.mimeType().split("/")[1];
        String videoFileName = UUID.randomUUID() + "." + extension;

        log.info("Save video to path: " + videoPath);
        if (!Files.exists(videoPath)) {
            Files.createDirectories(videoPath);
        }
        Path videoFilePath = videoPath.resolve(videoFileName);
        byte[] bytes = blobContent.byteStream().readAllBytes();
        Files.write(videoFilePath, bytes);

        // remove public from the path
        Path path = videoFilePath.subpath(1, videoFilePath.getNameCount());

        log.info("Save video success");

        return path;
    }

    public MediaDto createVideoMediaDto(String path, String eventId) {
        return MediaDto.builder()
                .type("VIDEO")
                .messageId(eventId)
                .path(path)
                .build();
    }
}
