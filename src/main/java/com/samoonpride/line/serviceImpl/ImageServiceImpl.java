package com.samoonpride.line.serviceImpl;

import com.linecorp.bot.client.base.BlobContent;
import com.linecorp.bot.messaging.client.MessagingApiBlobClient;
import com.samoonpride.line.dto.MediaDto;
import com.samoonpride.line.service.ImageService;
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
public class ImageServiceImpl implements ImageService {
    private final MessagingApiBlobClient lineMessagingClient;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public Path createImage(String userId, String eventId) throws ExecutionException, InterruptedException, IOException {
        log.info("Got event id: " + eventId);
        return saveImageToStorage(userId, lineMessagingClient.getMessageContent(eventId).get().body());
    }

    private Path saveImageToStorage(String userId, BlobContent blobContent) throws IOException {
        Path imagePath = Path.of("public/images/" + LocalDateTime.now().format(dateTimeFormatter) + "/" + userId);
        String extension = blobContent.mimeType().split("/")[1];
        String imageFileName = UUID.randomUUID() + "." + extension;

        log.info("Save image to path: " + imagePath);
        if (!Files.exists(imagePath)) {
            Files.createDirectories(imagePath);
        }

        byte[] bytes = blobContent.byteStream().readAllBytes();
        Path imageFilePath = imagePath.resolve(imageFileName);
        Files.write(imageFilePath, bytes);

        // remove public from the path
        Path path = imageFilePath.subpath(1, imageFilePath.getNameCount());

        log.info("Save image success");
        return path;
    }

    public MediaDto createImageMediaDto(String path, String eventId) {
        return MediaDto.builder()
                .type("IMAGE")
                .messageId(eventId)
                .path(path)
                .build();
    }
}
