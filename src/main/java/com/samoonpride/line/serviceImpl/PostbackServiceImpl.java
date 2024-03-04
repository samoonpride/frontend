package com.samoonpride.line.serviceImpl;

import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.messaging.model.TextMessage;
import com.linecorp.bot.webhook.model.PostbackContent;
import com.samoonpride.line.dto.PostbackResponse;
import com.samoonpride.line.dto.UserDto;
import com.samoonpride.line.dto.request.CreateIssueRequest;
import com.samoonpride.line.service.PostbackService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Log4j2
@AllArgsConstructor
@Service
public class PostbackServiceImpl implements PostbackService {
    private static final String[] POSTBACK_ACTIONS = {"duplicate", "subscribe","noDuplicate", "unsubscribe"};
    private SubscribeServiceImpl subscribeService;
    private IssueListServiceImpl issueListService;

    @Override
    public Message handlePostback(UserDto userDto, PostbackContent postbackContent) {
        log.info("Handling postback");
        PostbackResponse postbackResponse = mapPostbackContentToPostbackResponse(postbackContent);
        log.info("Postback response: " + postbackResponse);
        if (postbackResponse.getAction() == null) {
            log.info("Postback action is null");
            return new TextMessage("Postback action is null");
        } else if (postbackResponse.getAction().equals(POSTBACK_ACTIONS[0])) {
            return handleDuplicateIssue(userDto, postbackResponse);
        } else if (postbackResponse.getAction().equals(POSTBACK_ACTIONS[1])) {
            log.info("Subscribe issue");
            return handleSubscribeIssue(userDto, postbackResponse);
        } else if (postbackResponse.getAction().equals(POSTBACK_ACTIONS[2])) {
            log.info("No duplicate issue");
            return handleNoDuplicateIssue(userDto);
        } else if (postbackResponse.getAction().equals(POSTBACK_ACTIONS[3])) {
            log.info("Unsubscribe issue");
            return handleUnsubscribeIssue(userDto, postbackResponse);
        }
        return null;
    }

    private TextMessage handleDuplicateIssue(UserDto userDto, PostbackResponse postbackResponse) {
        log.info("Duplicate issue");
        CreateIssueRequest issueRequest = issueListService.findByUserId(userDto);
        issueRequest.setDuplicateIssueId(Integer.valueOf(postbackResponse.getIssueId()));
        issueListService.sendIssue(issueRequest);
        log.info("Duplicate issue success");
        log.info("Send issue success message");
        return new TextMessage("duplicate and create issue success");
    }

    private TextMessage handleSubscribeIssue(UserDto userDto, PostbackResponse postbackResponse) {
        subscribeService.subscribe(userDto.getUserId(), postbackResponse.getIssueId());
        log.info("Subscribe issue success");
        log.info("Send subscribe issue success message");
        return new TextMessage("subscribe issue success");
    }

    private TextMessage handleNoDuplicateIssue(UserDto userDto) {
        log.info("No duplicate issue");
        CreateIssueRequest issueRequest = issueListService.findByUserId(userDto);
        issueListService.sendIssue(issueRequest);
        log.info("No duplicate issue success");
        log.info("Send issue success message");
        return new TextMessage("no duplicate issue success");
    }

    private PostbackResponse mapPostbackContentToPostbackResponse(PostbackContent postbackContent) {
        JSONObject jsonObject = new JSONObject(postbackContent.data());
        log.info("Postback content: " + jsonObject);

        // Retrieve values from JSONObject and create PostbackResponse object
        String action = jsonObject.getString("action");
        if (jsonObject.has("issueId")) {
            String issueId = jsonObject.getString("issueId");
            return new PostbackResponse(action, issueId);
        }

        // Create PostbackResponse object
        return new PostbackResponse(action, null);
    }

    private TextMessage handleUnsubscribeIssue(UserDto userDto, PostbackResponse postbackResponse) {
        subscribeService.unsubscribe(userDto.getUserId(), postbackResponse.getIssueId());
        log.info("Unsubscribe issue success");
        log.info("Send unsubscribe issue success message");
        return new TextMessage("unsubscribe issue success");
    }
}
