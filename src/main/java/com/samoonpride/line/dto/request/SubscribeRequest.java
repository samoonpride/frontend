package com.samoonpride.line.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SubscribeRequest {
    private String lineUserId;
    private String issueId;
}
