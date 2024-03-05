package com.samoonpride.line.service;

import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.webhook.model.PostbackContent;
import com.samoonpride.line.dto.UserDto;

public interface PostbackService {
    Message handlePostback(UserDto userDto, PostbackContent postbackContent);
}
