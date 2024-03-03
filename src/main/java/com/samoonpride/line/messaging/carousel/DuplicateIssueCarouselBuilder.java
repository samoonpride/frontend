package com.samoonpride.line.messaging.carousel;

import com.linecorp.bot.messaging.model.FlexBubble;
import com.linecorp.bot.messaging.model.FlexCarousel;
import com.linecorp.bot.messaging.model.FlexMessage;
import com.samoonpride.line.dto.SimilarityBubbleDto;
import com.samoonpride.line.messaging.bubble.DuplicateIssueBubbleBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class DuplicateIssueCarouselBuilder {
    public static FlexMessage createIssueCarousel(List<SimilarityBubbleDto> similarityBubbleDtos) {
        List<FlexBubble> bubbles = similarityBubbleDtos.stream()
                .map(DuplicateIssueBubbleBuilder::createIssueBubble)
                .toList();
        log.info("Creating similarity carousel");
        FlexCarousel carousel = new FlexCarousel
                .Builder(bubbles)
                .build();
        log.info("Similarity carousel created: " + carousel);

        return new FlexMessage("Similar issues", carousel);
    }
}
