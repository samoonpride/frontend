package com.samoonpride.line.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageKeys {
    //# quick reply labels
    QUICK_REPLY_LABEL_NO_DUPLICATE_ISSUE("quick.reply.label.no.duplicate.issue"),
    QUICK_REPLY_LABEL_CAMERA("quick.reply.label.camera"),
    QUICK_REPLY_LABEL_LOCATION("quick.reply.label.location"),
    QUICK_REPLY_LABEL_CAMERA_ROLL("quick.reply.label.camera.roll"),
    //# quick reply messages
    QUICK_REPLY_MESSAGE_DUPLICATE_ISSUE("quick.reply.message.duplicate.issue"),
    QUICK_REPLY_MESSAGE_MEDIA("quick.reply.message.media"),
    QUICK_REPLY_MESSAGE_LOCATION("quick.reply.message.location"),
    //# normal messages
    MESSAGE_TITLE_MISSING("message.title.missing"),
    MESSAGE_ISSUE_NOT_FOUND("message.issue.not.found"),
    //# success messages
    SUCCESS_MESSAGE_SUBSCRIBED("success.message.subscribed"),
    SUCCESS_MESSAGE_UNSUBSCRIBED("success.message.unsubscribed"),
    SUCCESS_MESSAGE_CREATED_ISSUE("success.message.created.issue"),
    //# create issue messages
    CREATE_ISSUE_MESSAGE_CANCEL("create.issue.message.cancel"),
    CREATE_ISSUE_MESSAGE_CHANGE_TITLE("create.issue.message.change.title"),
    //# notification messages
    NOTIFICATION_MESSAGE_SUBSCRIBED_ISSUE("notification.message.subscribed.issue"),
    NOTIFICATION_MESSAGE_DUPLICATE_ISSUE("notification.message.duplicate.issue"),
    NOTIFICATION_MESSAGE_ISSUE_CREATION_TIMEOUT("notification.message.issue.creation.timeout");
    private final String key;
}