package com.samoonpride.line.serviceImpl;

import com.linecorp.bot.client.base.BlobContent;
import com.linecorp.bot.client.base.Result;
import com.linecorp.bot.messaging.client.MessagingApiBlobClient;
import com.samoonpride.line.dto.MediaDto;
import com.samoonpride.line.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
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
    public MediaDto createImage(String userId, String eventId) throws ExecutionException, InterruptedException, IOException {
        log.info("Got event id: " + eventId);
        String path = saveImageToStorage(userId, lineMessagingClient.getMessageContent(eventId).get().body());
        log.info("Save image success");
        MediaDto mediaDto = createMediaDto(path, eventId);
        log.info("Media dto: " + mediaDto.toString());
        log.info("Create media dto success");
        return mediaDto;
    }

    private String saveImageToStorage(String userId, BlobContent blobContent) throws IOException {
        Path path = Path.of( "public/images/" + LocalDateTime.now().format(dateTimeFormatter)+ "/" + userId);
        String extension = blobContent.mimeType().split("/")[1];
        String fileName = UUID.randomUUID() + "." + extension;

        log.info("Save image to path: " + path);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        byte[] bytes = blobContent.byteStream().readAllBytes();

        Files.write(path.resolve(fileName), bytes);

        return path + "/" + fileName;
    }

    private MediaDto createMediaDto(String path, String eventId) {
        return MediaDto.builder()
                .type("IMAGE")
                .messageId(eventId)
                .path(path)
                .build();
    }
}
