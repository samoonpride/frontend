package com.samoonpride.line.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageConfig {
    @Getter
    private static int thumbnailHeight;
    @Getter
    private static int thumbnailWidth;

    @Value("${thumbnail.height}")
    public void setThumbnailHeight(String height) {
        thumbnailHeight = Integer.parseInt(height);
    }

    @Value("${thumbnail.width}")
    public void setThumbnailWidth(String width) {
        thumbnailWidth = Integer.parseInt(width);
    }
}
