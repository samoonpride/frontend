package com.samoonpride.line.utils;

import com.samoonpride.line.config.ImageConfig;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Log4j2
@UtilityClass
public class ThumbnailUtils {
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
                        .size(ImageConfig.getThumbnailWidth(), ImageConfig.getThumbnailHeight())
                        .keepAspectRatio(true)
                        .outputFormat("jpeg")
                        .toFile(thumbnailFile);
            }
        } catch (IOException e) {
            log.error("Error creating thumbnail: " + e.getMessage());
        }
        log.info("Thumbnail created: " + thumbnailFile.getName());

        // remove public from the path
        Path path = thumbnailFile.toPath().subpath(1, thumbnailFile.toPath().getNameCount());
        return path.toString();
    }
}
