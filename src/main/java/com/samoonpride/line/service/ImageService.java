package com.samoonpride.line.service;

import com.samoonpride.line.dto.MediaDto;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface ImageService {
    MediaDto createImage(String userId, String eventId) throws ExecutionException, InterruptedException, IOException;
}
