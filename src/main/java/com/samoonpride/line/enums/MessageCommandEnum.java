package com.samoonpride.line.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum MessageCommandEnum {
    LATEST_ISSUE("Latest Issue"),
    SUBSCRIBE_ISSUE("Subscribe Issue"),
    MY_ISSUE("My Issue");

    private final String value;

    public static String matchCommand(String command) {
        for (MessageCommandEnum messageCommandEnum : MessageCommandEnum.values()) {
            if (messageCommandEnum.getValue().equalsIgnoreCase(command)) {
                return messageCommandEnum.getValue();
            }
        }
        return null;
    }

    public static List<String> getCommands() {
        return Arrays.stream(MessageCommandEnum.values())
                .map(MessageCommandEnum::getValue)
                .collect(Collectors.toList());
    }
}
