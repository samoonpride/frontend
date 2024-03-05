package com.samoonpride.line.messaging;

import com.linecorp.bot.messaging.model.*;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;

import static com.samoonpride.line.config.MessageSourceConfig.getMessage;
import static com.samoonpride.line.enums.MessageKeys.*;

public class QuickReplyBuilder {
    public static TextMessage createNoDuplicateQuickReplyMessage() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", "noDuplicate");
        Action action = new PostbackAction
                .Builder()
                .label(getMessage(QUICK_REPLY_LABEL_NO_DUPLICATE_ISSUE))
                .data(jsonObject.toString())
                .build();

        QuickReplyItem quickReplyItem = new QuickReplyItem
                .Builder()
                .action(action)
                .build();

        QuickReply quickReply = new QuickReply
                .Builder()
                .items(Collections.singletonList(quickReplyItem))
                .build();

        return new TextMessage.Builder(getMessage(QUICK_REPLY_MESSAGE_DUPLICATE_ISSUE)).quickReply(
                quickReply
        ).build();
    }

    public static TextMessage createLocationQuickReplyMessage() {
        Action action = new LocationAction
                .Builder()
                .label(getMessage(QUICK_REPLY_LABEL_LOCATION))
                .build();

        QuickReplyItem quickReplyItem = new QuickReplyItem
                .Builder()
                .action(action)
                .build();

        QuickReply quickReply = new QuickReply
                .Builder()
                .items(Collections.singletonList(quickReplyItem))
                .build();

        return new TextMessage.Builder(getMessage(QUICK_REPLY_MESSAGE_LOCATION)).quickReply(
                quickReply
        ).build();
    }

    public static TextMessage createMediaQuickReplyMessage() {
        QuickReply quickReply = new QuickReply
                .Builder()
                .items(Arrays.asList(
                        createCameraQuickReply(),
                        createCameraRollQuickReply()
                ))
                .build();

        return new TextMessage.Builder(getMessage(QUICK_REPLY_MESSAGE_MEDIA)).quickReply(
                quickReply
        ).build();
    }

    private static QuickReplyItem createCameraQuickReply() {
        Action action = new CameraAction
                .Builder()
                .label(getMessage(QUICK_REPLY_LABEL_CAMERA))
                .build();

        return new QuickReplyItem
                .Builder()
                .action(action)
                .build();
    }

    private static QuickReplyItem createCameraRollQuickReply() {
        Action action = new CameraRollAction
                .Builder()
                .label(getMessage(QUICK_REPLY_LABEL_CAMERA_ROLL))
                .build();

        return new QuickReplyItem
                .Builder()
                .action(action)
                .build();
    }
}
