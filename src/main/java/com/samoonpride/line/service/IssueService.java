package com.samoonpride.line.service;

import com.linecorp.bot.messaging.model.TextMessage;
import com.linecorp.bot.webhook.model.MessageContent;
import com.samoonpride.line.dto.UserDto;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public interface IssueService {
    TextMessage createIssue(UserDto userDto, MessageContent message) throws IOException, ExecutionException, InterruptedException;
}
