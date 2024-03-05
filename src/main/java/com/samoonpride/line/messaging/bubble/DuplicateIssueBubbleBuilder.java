package com.samoonpride.line.messaging.bubble;

import com.linecorp.bot.messaging.model.*;
import com.samoonpride.line.config.AppConfig;
import com.samoonpride.line.dto.IssueBubbleDto;
import com.samoonpride.line.dto.SimilarityBubbleDto;
import com.samoonpride.line.enums.PostbackActionEnum;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Collections;
import java.util.Optional;

import static java.util.Collections.singletonList;


@AllArgsConstructor
@Component
@Log4j2
public class DuplicateIssueBubbleBuilder {
    public static FlexBubble createIssueBubble(SimilarityBubbleDto similarityBubbleDto) {
        log.info("Creating similarity bubble");
        IssueBubbleDto issueBubbleDto = similarityBubbleDto.getIssueBubbleDto();
        String duplicateId = String.valueOf(Optional.ofNullable(similarityBubbleDto.getDuplicateIssueId())
                .orElse(similarityBubbleDto.getIssueId()));
        log.info("Duplicate issue id: " + duplicateId);
        FlexBubble bubble = new FlexBubble
                .Builder()
                .header(createHeaderBlock(issueBubbleDto))
                .hero(createHeroBlock(issueBubbleDto))
                .body(createBodyBlock(issueBubbleDto))
                .footer(createFooterBlock(duplicateId))
                .build();
        log.info("Similarity bubble created: " + bubble);
        return bubble;
    }

    private static FlexBox createHeaderBlock(IssueBubbleDto issueBubbleDto) {
        log.info("Creating header block");
        FlexText title = new FlexText.Builder().text(issueBubbleDto.getTitle()).size("xl").wrap(true).align(FlexText.Align.CENTER).build();

        return new FlexBox.Builder(FlexBox.Layout.HORIZONTAL, singletonList(title)).build();
    }

    private static FlexBox createBodyBlock(IssueBubbleDto issueBubbleDto) {
        log.info("Creating body block");
        FlexText status = new FlexText.Builder().text("Status: " + issueBubbleDto.getStatus().toString()).size("sm").align(FlexText.Align.START).build();

        return new FlexBox.Builder(FlexBox.Layout.VERTICAL, singletonList(status)).build();
    }

    private static FlexImage createHeroBlock(IssueBubbleDto issueBubbleDto) {
        log.info("Creating hero block");
        try {
            URI uri = new URI(AppConfig.getAppUrl() + "/" + issueBubbleDto.getThumbnailPath());
            return new FlexImage.Builder(uri).size("full").aspectRatio("1.5:1").aspectMode(FlexImage.AspectMode.FIT).build();
        } catch (Exception e) {
            log.error("Error creating hero block: " + e.getMessage());
            return null;
        }
    }

    private static FlexBox createFooterBlock(String duplicateId) {
        // Create JSON object for the action
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", "duplicate");
        jsonObject.put("issueId", duplicateId);

        log.info("Creating JSON object: " + jsonObject);

        log.info("Creating footer block");
        Action action = new PostbackAction.Builder().label(PostbackActionEnum.DUPLICATE.toString()).data(jsonObject.toString()).build();
        log.info("Subscribe action created: " + action);

        FlexButton button = new FlexButton.Builder(action).build();

        return new FlexBox.Builder(FlexBox.Layout.HORIZONTAL, Collections.singletonList(button)).build();
    }
}
