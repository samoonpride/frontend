package com.samoonpride.line.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueDto {
    private UserDto user;
    private String title;
    private Double latitude;
    private Double longitude;
    private String thumbnailPath;
    private List<MediaDto> media = new ArrayList<>();

    public IssueDto(UserDto userDto) {
        this.user = userDto;
    }
}
