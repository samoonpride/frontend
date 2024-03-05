package com.samoonpride.line.messaging.carousel;

import com.linecorp.bot.messaging.model.FlexBubble;
import com.linecorp.bot.messaging.model.FlexCarousel;
import com.linecorp.bot.messaging.model.FlexMessage;
import com.samoonpride.line.dto.IssueBubbleDto;
import com.samoonpride.line.messaging.bubble.IssueBubbleBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class IssueCarouselBuilder {
    public static FlexMessage createIssueCarousel(List<IssueBubbleDto> issueBubbleDtoList) {
        List<FlexBubble> bubbles = issueBubbleDtoList.stream()
                .map(IssueBubbleBuilder::createIssueBubble)
                .toList();
        log.info("Creating issue carousel");
        FlexCarousel carousel = new FlexCarousel
                .Builder(bubbles)
                .build();
        log.info("Issue carousel created: " + carousel);
        return new FlexMessage("Issues", carousel);
    }
}
