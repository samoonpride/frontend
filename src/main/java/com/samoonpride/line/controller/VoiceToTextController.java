package com.samoonpride.line.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.linecorp.bot.client.base.BlobContent;
import com.linecorp.bot.messaging.client.MessagingApiBlobClient;
import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.messaging.model.TextMessage;
import com.linecorp.bot.spring.boot.handler.annotation.EventMapping;
import com.linecorp.bot.spring.boot.handler.annotation.LineMessageHandler;
import com.linecorp.bot.webhook.model.AudioMessageContent;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@RestController
@LineMessageHandler
public class VoiceToTextController {
    private final MessagingApiBlobClient lineMessagingClient;
    private final RestTemplate restTemplate;
    private static final String VOICE_TO_TEXT_URL = "http://voice-to-text:8001/voice-to-text";

    public VoiceToTextController(MessagingApiBlobClient lineMessagingClient) {
        this.lineMessagingClient = lineMessagingClient;
        this.restTemplate = new RestTemplate();
    }

	@EventMapping
    public Message handleAudioMessage(AudioMessageContent event) throws ExecutionException, InterruptedException, IOException {
        final String messageId = event.id();
        System.out.printf("Got audio message %s\n", messageId);
        String result = sendAudioToVoiceToText(lineMessagingClient.getMessageContent(messageId).get().body());
        System.out.printf("Got result %s\n", result);
		return new TextMessage(result);
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
        ResponseEntity<String> responseEntity = restTemplate.exchange(VOICE_TO_TEXT_URL, HttpMethod.POST, requestEntity, String.class);

        // parse json and get text
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(responseEntity.getBody());

        return jsonNode.get("text").asText();
    }
}
