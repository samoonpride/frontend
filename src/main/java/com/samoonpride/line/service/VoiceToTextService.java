package com.samoonpride.line.service;

import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.webhook.model.AudioMessageContent;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface VoiceToTextService {
    Message handleAudioMessage(AudioMessageContent audioMessageContent) throws IOException, ExecutionException, InterruptedException;
}
