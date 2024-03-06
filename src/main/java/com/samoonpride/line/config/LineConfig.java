package com.samoonpride.line.config;

import com.linecorp.bot.messaging.client.MessagingApiClient;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LineConfig {
    @Getter
    private static MessagingApiClient messagingApiClient;

    @Value("${line.bot.channel-token}")
    public void setMessagingApiClient(String channelAccessToken) {
        messagingApiClient = MessagingApiClient.builder(channelAccessToken).build();
    }
}
