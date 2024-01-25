package com.samoonpride.line.service;

import com.samoonpride.line.dto.ReportDto;
import com.samoonpride.line.dto.UserDto;

public interface ReportListService {
    void addReport(ReportDto reportDto);
    ReportDto findByUserId(UserDto userDto);
    void sendReport(ReportDto reportDto);
}
