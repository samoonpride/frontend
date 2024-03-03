package com.samoonpride.line.messaging.bubble;

import com.linecorp.bot.messaging.model.*;
import com.samoonpride.line.config.AppConfig;
import com.samoonpride.line.dto.IssueBubbleDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Collections;

import static java.util.Collections.singletonList;

@AllArgsConstructor
@Component
@Log4j2
public class IssueBubbleBuilder {

    private static final String SUBSCRIBE_LABEL = "subscribe";

    public static FlexBubble createIssueBubble(IssueBubbleDto issueBubbleDto) {
        log.info("Creating issue bubble");
        FlexBubble bubble = new FlexBubble
                .Builder()
                .header(createHeaderBlock(issueBubbleDto))
                .hero(createHeroBlock(issueBubbleDto))
                .body(createBodyBlock(issueBubbleDto))
                .footer(createFooterBlock(issueBubbleDto))
                .build();
        log.info("Issue bubble created: " + bubble);
        return bubble;
    }

    private static FlexBox createHeaderBlock(IssueBubbleDto issueBubbleDto) {
        log.info("Creating header block");
        FlexText title = new FlexText
                .Builder()
                .text(issueBubbleDto.getTitle())
                .size("xl")
                .wrap(true)
                .align(FlexText.Align.CENTER)
                .build();

        return new FlexBox
                .Builder(FlexBox.Layout.HORIZONTAL, singletonList(title))
                .build();
    }

    private static FlexBox createBodyBlock(IssueBubbleDto issueBubbleDto) {
        log.info("Creating body block");
        FlexText status = new FlexText
                .Builder()
                .text("Status: " + issueBubbleDto.getStatus().toString())
                .size("sm")
                .align(FlexText.Align.START)
                .build();

        return new FlexBox
                .Builder(FlexBox.Layout.VERTICAL, singletonList(status))
                .build();
    }

    private static FlexImage createHeroBlock(IssueBubbleDto issueBubbleDto) {
        log.info("Creating hero block");
        try {
            URI uri = new URI(AppConfig.getAppUrl() + "/" + issueBubbleDto.getThumbnailPath());
            return new FlexImage
                    .Builder(uri)
                    .size("full")
                    .aspectRatio("1.5:1")
                    .aspectMode(FlexImage.AspectMode.FIT)
                    .build();
        } catch (Exception e) {
            log.error("Error creating hero block: " + e.getMessage());
            return null;
        }
    }

    private static FlexBox createFooterBlock(IssueBubbleDto issueBubbleDto) {
        // Create json object for postback action
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", "subscribe");
        jsonObject.put("issueId", issueBubbleDto.getIssueId());
        log.info("Creating json object: " + jsonObject);


        log.info("Creating footer block");
        Action action = new PostbackAction
                .Builder()
                .label(SUBSCRIBE_LABEL)
                .data(String.valueOf(issueBubbleDto.getIssueId()))
                .build();
        log.info("Subscribe action created: " + action);

        FlexButton button = new FlexButton
                .Builder(action)
                .build();

        return new FlexBox
                .Builder(FlexBox.Layout.HORIZONTAL, Collections.singletonList(button))
                .build();
    }
}
