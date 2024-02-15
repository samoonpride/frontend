package com.samoonpride.line.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;

@Log4j2
@UtilityClass
public class ThumbnailUtils {
    private final int width = 640;
    private final int height = 640;

    public String createThumbnail(File file) {
        log.info("Creating thumbnail for file: " + file.getName());
        File thumbnailFile = new File("public/thumbnail/" + "thumbnail_" + file.getName());
        try {
            if (!thumbnailFile.exists()) {
                // create directory if not exists
                if (!thumbnailFile.getParentFile().exists()) {
                    thumbnailFile.getParentFile().mkdirs();
                }
                Thumbnails.of(file)
                        .size(width, height)
                        .outputFormat("jpeg")
                        .toFile(thumbnailFile);
            }
        } catch (IOException e) {
            log.error("Error creating thumbnail: " + e.getMessage());
        }
        log.info("Thumbnail created: " + thumbnailFile.getName());
        return thumbnailFile.toPath().toString();
    }
}
