package com.samoonpride.line.service;

import com.samoonpride.line.dto.IssueBubbleDto;
import com.samoonpride.line.dto.request.CreateIssueRequest;
import com.samoonpride.line.dto.UserDto;

import java.util.List;

public interface IssueListService {
    void addIssue(CreateIssueRequest createIssueRequest);

    CreateIssueRequest findByUserId(UserDto userDto);

    void sendIssue(CreateIssueRequest createIssueRequest);

    List<IssueBubbleDto> getLatestSelfIssues(String userId);

    List<IssueBubbleDto> getIssuesByDistinctUser(String userId);

    List<IssueBubbleDto> getSubscribedIssues(String userId);
}
