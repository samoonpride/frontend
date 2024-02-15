package com.samoonpride.line.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;

public interface VideoService {
    Path createVideo(String userId, String eventId) throws ExecutionException, InterruptedException, IOException;
}
