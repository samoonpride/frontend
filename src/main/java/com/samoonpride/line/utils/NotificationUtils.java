package com.samoonpride.line.utils;

import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.messaging.model.PushMessageRequest;
import com.linecorp.bot.messaging.model.TextMessage;
import com.samoonpride.line.config.LineConfig;
import com.samoonpride.line.dto.NotificationBubbleDto;
import com.samoonpride.line.dto.request.CreateIssueRequest;
import com.samoonpride.line.messaging.bubble.NotificationBubbleBuilder;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.samoonpride.line.config.MessageSourceConfig.getMessage;
import static com.samoonpride.line.enums.MessageKeys.*;

@UtilityClass
public class NotificationUtils {
    public static void notificationUpdateStatusIssue(List<NotificationBubbleDto> notificationBubbleDtoList) {
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

    public static void notificationIssueTimeout(CreateIssueRequest createIssueRequest) {
        TextMessage textMessage = new TextMessage(getMessage(NOTIFICATION_MESSAGE_ISSUE_CREATION_TIMEOUT));
        PushMessageRequest pushMessageRequest = new PushMessageRequest
                .Builder(createIssueRequest.getUser().getUserId(), Collections.singletonList(textMessage))
                .build();
        LineConfig.getMessagingApiClient()
                .pushMessage(UUID.randomUUID(), pushMessageRequest);
    }
}
