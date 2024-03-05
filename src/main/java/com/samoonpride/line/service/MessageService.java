package com.samoonpride.line.service;

import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.webhook.model.MessageContent;
import com.samoonpride.line.dto.UserDto;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface MessageService {
    List<Message> handleMessage(UserDto userDto, MessageContent message) throws IOException, ExecutionException, InterruptedException;
}
