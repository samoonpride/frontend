package com.samoonpride.line.serviceImpl;

import com.linecorp.bot.client.base.BlobContent;
import com.linecorp.bot.messaging.client.MessagingApiBlobClient;
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
    public void createVideo(String userId, String eventId) throws ExecutionException, InterruptedException, IOException {
        log.info("Got event id: " + eventId);
        saveVideoToStorage(userId, lineMessagingClient.getMessageContent(eventId).get().body());
        log.info("Save video success");
    }

    private void saveVideoToStorage(String userId, BlobContent blobContent) throws IOException {
        Path path = Path.of( "public/videos/" + LocalDateTime.now().format(dateTimeFormatter)+ "/" + userId);
        String extension = blobContent.mimeType().split("/")[1];
        String fileName = UUID.randomUUID() + "." + extension;

        log.info("Save video to path: " + path);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        byte[] bytes = blobContent.byteStream().readAllBytes();

        Files.write(path.resolve(fileName), bytes);
    }

}
