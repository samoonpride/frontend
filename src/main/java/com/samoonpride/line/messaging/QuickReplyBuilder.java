package com.samoonpride.line.messaging;

import com.linecorp.bot.messaging.model.*;
import org.json.JSONObject;

import java.util.Collections;

public class QuickReplyBuilder {
    private static final String NO_DUPLICATE_ISSUE_MESSAGE = "ไม่พบปัญหาที่คล้ายคลึงกัน";
    private static final String DUPLICATE_ISSUE_MESSAGE = "กรุณาตรวจสอบว่าปัญหาที่คุณรายงานมีความคล้ายคลึงกับปัญหาที่มีอยู่แล้วหรือไม่";

    public static Message createNoDuplicateQuickReply() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", "noDuplicate");

        Action action = new PostbackAction
                .Builder()
                .label(NO_DUPLICATE_ISSUE_MESSAGE)
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

        return new TextMessage.Builder(DUPLICATE_ISSUE_MESSAGE).quickReply(
                quickReply
        ).build();
    }
}
