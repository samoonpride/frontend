package com.samoonpride.line.messaging;

import com.linecorp.bot.messaging.model.*;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;

public class QuickReplyBuilder {
    private static final String NO_DUPLICATE_ISSUE_LABEL = "ไม่พบปัญหาที่คล้ายคลึงกัน";
    private static final String MEDIA_MESSAGE = "กรุณาใส่รูปภาพหรือวิดีโอ";
    private static final String LOCATION_MESSAGE = "กรุณาใส่ตำแหน่งที่อยู่";
    private static final String CAMERA_LABEL = "ถ่ายรูปหรือวิดีโอ";
    private static final String LOCATION_LABEL = "ส่งตำแหน่งที่อยู่";
    private static final String CAMERA_ROLL_LABEL = "เลือกรูปภาพจากแกลเลอรี";
    private static final String DUPLICATE_ISSUE_MESSAGE = "กรุณาตรวจสอบว่าปัญหาที่คุณรายงานมีความคล้ายคลึงกับปัญหาที่มีอยู่แล้วหรือไม่";

    public static TextMessage createNoDuplicateQuickReplyMessage() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", "noDuplicate");

        Action action = new PostbackAction
                .Builder()
                .label(NO_DUPLICATE_ISSUE_LABEL)
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

    public static TextMessage createLocationQuickReplyMessage() {
        Action action = new LocationAction
                .Builder()
                .label(LOCATION_LABEL)
                .build();

        QuickReplyItem quickReplyItem = new QuickReplyItem
                .Builder()
                .action(action)
                .build();

        QuickReply quickReply = new QuickReply
                .Builder()
                .items(Collections.singletonList(quickReplyItem))
                .build();

        return new TextMessage.Builder(LOCATION_MESSAGE).quickReply(
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

        return new TextMessage.Builder(MEDIA_MESSAGE).quickReply(
                quickReply
        ).build();
    }

    private static QuickReplyItem createCameraQuickReply() {
        Action action = new CameraAction
                .Builder()
                .label(CAMERA_LABEL)
                .build();

        return new QuickReplyItem
                .Builder()
                .action(action)
                .build();
    }

    private static QuickReplyItem createCameraRollQuickReply() {
        Action action = new CameraRollAction
                .Builder()
                .label(CAMERA_ROLL_LABEL)
                .build();

        return new QuickReplyItem
                .Builder()
                .action(action)
                .build();
    }
}
