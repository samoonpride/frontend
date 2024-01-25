package com.samoonpride.line.service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface VideoService {
    void createVideo(String userId, String eventId) throws ExecutionException, InterruptedException, IOException;
}
