package com.samoonpride.line.dto;

import com.samoonpride.line.enums.IssueStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueBubbleDto {
    int issueId;
    private String title;
    private String thumbnailPath;
    private IssueStatus status;

    @Override
    public String toString() {
        return "title='" + title + '\'' + "\n" +
                "thumbnailPath='" + thumbnailPath + '\'' + "\n" +
                "status=" + status + "\n";
    }
}