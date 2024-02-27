package com.samoonpride.line.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SentenceSimilarityCheckerRequest {
    private String sourceSentence;
    private List<String> sentences;
}
