package com.samoonpride.line.serviceImpl;

import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.messaging.model.PushMessageRequest;
import com.linecorp.bot.messaging.model.TextMessage;
import com.samoonpride.line.config.LineConfig;
import com.samoonpride.line.dto.NotificationBubbleDto;
import com.samoonpride.line.messaging.bubble.NotificationBubbleBuilder;
import com.samoonpride.line.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.samoonpride.line.config.MessageSourceConfig.getMessage;
import static com.samoonpride.line.enums.MessageKeys.NOTIFICATION_MESSAGE_DUPLICATE_ISSUE;
import static com.samoonpride.line.enums.MessageKeys.NOTIFICATION_MESSAGE_SUBSCRIBED_ISSUE;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    @Override
    public void notificationIssue(List<NotificationBubbleDto> notificationBubbleDtoList) {
        for (NotificationBubbleDto notificationBubbleDto : notificationBubbleDtoList) {
            for (String lineUserId : notificationBubbleDto.getLineUserIds()) {

                TextMessage textMessage = notificationBubbleDto.isSubscribed() ?
                        new TextMessage(getMessage(NOTIFICATION_MESSAGE_SUBSCRIBED_ISSUE)) :
                        new TextMessage(getMessage(NOTIFICATION_MESSAGE_DUPLICATE_ISSUE));

                List<Message> messages = Arrays.asList(
                        NotificationBubbleBuilder.createNotifyIssueBubble(notificationBubbleDto),
                        textMessage
                );

                PushMessageRequest pushMessageRequest = new PushMessageRequest
                        .Builder(lineUserId, messages)
                        .build();

                LineConfig.getMessagingApiClient()
                        .pushMessage(UUID.randomUUID(), pushMessageRequest);
            }
        }
    }
}