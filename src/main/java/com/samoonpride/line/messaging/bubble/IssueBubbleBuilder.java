package com.samoonpride.line.messaging.bubble;

import com.linecorp.bot.messaging.model.*;
import com.samoonpride.line.config.AppConfig;
import com.samoonpride.line.dto.IssueBubbleDto;
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
public class IssueBubbleBuilder {

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

        Optional<Boolean> isSubscribed = Optional.ofNullable(issueBubbleDto.getSubscribed());
        log.info("Is subscribed: " + isSubscribed);
        // when isSubscribed is null, it means issue is from user's issue list
        // and is not have footer block
        if (isSubscribed.isEmpty()) {
            return null;
        }

        String actionString = isSubscribed
                .map(subscribed -> subscribed ? PostbackActionEnum.UNSUBSCRIBE.getValue() : PostbackActionEnum.SUBSCRIBE.getValue())
                .orElse(PostbackActionEnum.DUPLICATE.getValue());
        jsonObject.put("action", actionString);
        jsonObject.put("issueId", String.valueOf(issueBubbleDto.getIssueId()));
        log.info("Creating json object: " + jsonObject);


        log.info("Creating footer block");
        Action action = new PostbackAction.Builder().label(actionString).data(jsonObject.toString()).build();
        log.info("Subscribe action created: " + action);

        FlexButton button = new FlexButton.Builder(action).build();

        return new FlexBox.Builder(FlexBox.Layout.HORIZONTAL, Collections.singletonList(button)).build();
    }
}
