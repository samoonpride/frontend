package com.samoonpride.line.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {
    private UserDto user;
    private String title;
    private Double latitude;
    private Double longitude;
    private List<MediaDto> media = new ArrayList<>();

    public ReportDto(UserDto userDto) {
        this.user = userDto;
    }
}
