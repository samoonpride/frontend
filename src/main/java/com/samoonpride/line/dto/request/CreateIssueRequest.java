package com.samoonpride.line.dto.request;

import com.samoonpride.line.dto.MediaDto;
import com.samoonpride.line.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateIssueRequest {
    private UserDto user;
    private Integer duplicateIssueId;
    private String title;
    private Double latitude;
    private Double longitude;
    private List<MediaDto> media = new ArrayList<>();

    public CreateIssueRequest(UserDto userDto) {
        this.user = userDto;
    }
}
