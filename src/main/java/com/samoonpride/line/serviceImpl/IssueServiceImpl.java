package com.samoonpride.line.serviceImpl;

import com.linecorp.bot.messaging.model.TextMessage;
import com.samoonpride.line.config.MessageSourceConfig;
import com.samoonpride.line.dto.request.CreateIssueRequest;
import com.samoonpride.line.messaging.QuickReplyBuilder;
import com.samoonpride.line.service.IssueService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import static com.samoonpride.line.enums.MessageKeys.MESSAGE_TITLE_MISSING;

@Log4j2
@AllArgsConstructor
@Service
public class IssueServiceImpl implements IssueService {
    public boolean isIssueComplete(CreateIssueRequest issue) {
        return issue.getTitle() != null && !issue.getMedia().isEmpty() && issue.getLatitude() != null && issue.getLongitude() != null;
    }

    public TextMessage generateIssueIncompleteMessage(CreateIssueRequest issue) {
        if (issue.getTitle() == null) {
            log.info("Issue not have title");
            return new TextMessage(MessageSourceConfig.getMessage(MESSAGE_TITLE_MISSING));
        } else if (issue.getMedia().isEmpty()) {
            log.info("Issue not have media");
            return QuickReplyBuilder.createMediaQuickReplyMessage();
        } else if (issue.getLatitude() == null || issue.getLongitude() == null) {
            log.info("Issue not have location");
            return QuickReplyBuilder.createLocationQuickReplyMessage();
        }
        return null;
    }
}
