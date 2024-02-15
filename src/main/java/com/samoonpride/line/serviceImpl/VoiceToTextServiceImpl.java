package com.samoonpride.line.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.client.base.BlobContent;
import com.linecorp.bot.messaging.client.MessagingApiBlobClient;
import com.linecorp.bot.webhook.model.AudioMessageContent;
import com.samoonpride.line.config.ApiConfig;
import com.samoonpride.line.service.VoiceToTextService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class VoiceToTextServiceImpl implements VoiceToTextService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final MessagingApiBlobClient lineMessagingClient;
    private final ApiConfig apiConfig;

    public String handleAudioMessage(AudioMessageContent event) throws IOException, ExecutionException, InterruptedException {
        System.out.printf("Got audio message %s\n", event.id());
        BlobContent blobContent = lineMessagingClient.getMessageContent(event.id()).get().body();
        String result = sendAudioToVoiceToText(blobContent);
        System.out.printf("Got result %s\n", result);
        return result;
    }

    private String sendAudioToVoiceToText(BlobContent blobContent) throws IOException {
        // Read audio content from BlobContent
        byte[] bytes = blobContent.byteStream().readAllBytes();

        // Create ByteArrayResource with audio content
        ByteArrayResource resource = new ByteArrayResource(bytes) {
            @Override
            public String getFilename() {
                return "audio.m4a";
            }
        };

        // Create HttpHeaders with the appropriate content type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Create MultipartBodyBuilder with the audio file
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("file", resource);

        // Create RestTemplate and send the file to the Python microservice
        HttpEntity<MultiValueMap<String, HttpEntity<?>>> requestEntity = new HttpEntity<>(bodyBuilder.build(), headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                apiConfig.apiVoiceToTextUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // parse json and get text
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(responseEntity.getBody());

        return jsonNode.get("text").asText();
    }
}
