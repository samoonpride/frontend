package com.samoonpride.line.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SubscribeRequest {
    private String lineUserId;
    private String issueId;
}
