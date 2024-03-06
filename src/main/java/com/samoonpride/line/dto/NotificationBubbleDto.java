package com.samoonpride.line.dto;

import com.samoonpride.line.enums.IssueStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationBubbleDto {
    private List<String> lineUserIds;
    private String title;
    private String thumbnailPath;
    private IssueStatus status;
    private boolean subscribed;
}
