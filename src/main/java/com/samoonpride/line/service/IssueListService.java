package com.samoonpride.line.service;

import com.samoonpride.line.dto.IssueBubbleDto;
import com.samoonpride.line.dto.IssueDto;
import com.samoonpride.line.dto.UserDto;

import java.util.List;

public interface IssueListService {
    void addIssue(IssueDto issueDto);

    IssueDto findByUserId(UserDto userDto);

    void sendIssue(IssueDto issueDto);

    List<IssueBubbleDto> getLatestIssues(String userId);
}
