package com.samoonpride.line.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueSimilarityBubble {
    private int issueId;
    private Integer duplicateIssueId;
    private float similarityScore;
    private IssueBubbleDto issueBubbleDto;

    //    To string
    public String toString() {
        return "IssueSimilarityBubble {" +
            "\n\tissueId=" + issueId +
            ",\n\tduplicateIssueId=" + duplicateIssueId +
            ",\n\tsimilarityScore=" + similarityScore +
            ",\n\tissueBubble=" + (issueBubbleDto != null ? issueBubbleDto.toString() : "null") +
            "\n}";
    }

}

