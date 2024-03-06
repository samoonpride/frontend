package com.samoonpride.line.serviceImpl;

import com.samoonpride.line.service.IssueListService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class ScheduledTaskServiceImpl {
    private final IssueListService issueListService;

    @Scheduled(fixedDelayString = "${issue.creation.timeout}")
    public void deleteCreatedIssueTimeout() {
        log.info("deleteCreatedIssueTimeout");
        issueListService.deleteCreatedIssueTimeout();
        log.info("deleteCreatedIssueTimeout done");
    }
}
