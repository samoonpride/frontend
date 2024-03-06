package com.samoonpride.line.messaging.bubble;

import com.linecorp.bot.messaging.model.*;
import com.samoonpride.line.config.AppConfig;
import com.samoonpride.line.dto.NotificationBubbleDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.net.URI;

import static java.util.Collections.singletonList;

@AllArgsConstructor
@Component
@Log4j2
public class NotificationBubbleBuilder {
    public static FlexMessage createNotifyIssueBubble(NotificationBubbleDto notificationBubbleDto) {
        log.info("Creating issue notify bubble");
        FlexBubble bubble = new FlexBubble
                .Builder()
                .header(createHeaderBlock(notificationBubbleDto))
                .hero(createHeroBlock(notificationBubbleDto))
                .body(createBodyBlock(notificationBubbleDto))
                .build();
        log.info("Issue issue notify bubble created: " + bubble);
        return new FlexMessage("Notify Issue", bubble);
    }

    private static FlexBox createHeaderBlock(NotificationBubbleDto notificationBubbleDto) {
        log.info("Creating header block");
        FlexText title = new FlexText
                .Builder()
                .text(notificationBubbleDto.getTitle())
                .size("xl")
                .wrap(true)
                .align(FlexText.Align.CENTER)
                .build();

        return new FlexBox
                .Builder(FlexBox.Layout.HORIZONTAL, singletonList(title))
                .build();
    }

    private static FlexBox createBodyBlock(NotificationBubbleDto notificationBubbleDto) {
        log.info("Creating body block");
        FlexText status = new FlexText
                .Builder()
                .text("Status: " + notificationBubbleDto.getStatus().toString())
                .size("sm")
                .align(FlexText.Align.START)
                .build();

        return new FlexBox
                .Builder(FlexBox.Layout.VERTICAL, singletonList(status))
                .build();
    }

    private static FlexImage createHeroBlock(NotificationBubbleDto notificationBubbleDto) {
        log.info("Creating hero block");
        try {
            URI uri = new URI(AppConfig.getBackendUrl() + "/" + notificationBubbleDto.getThumbnailPath());
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
}
