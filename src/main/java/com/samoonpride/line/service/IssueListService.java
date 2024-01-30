package com.samoonpride.line.service;

import com.samoonpride.line.dto.IssueDto;
import com.samoonpride.line.dto.UserDto;

public interface IssueListService {
    void addIssue(IssueDto issueDto);
    IssueDto findByUserId(UserDto userDto);
    void sendIssue(IssueDto issueDto);
}
