package com.samoonpride.line.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostbackResponse {
    private String action;
    private String issueId;
}
