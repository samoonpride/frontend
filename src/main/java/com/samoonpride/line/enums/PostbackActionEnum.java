package com.samoonpride.line.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostbackActionEnum {
    DUPLICATE("duplicate"),
    NO_DUPLICATE("noDuplicate"),
    SUBSCRIBE("subscribe"),
    UNSUBSCRIBE("unsubscribe");

    private final String value;
}
