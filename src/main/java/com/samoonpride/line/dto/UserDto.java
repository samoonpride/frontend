package com.samoonpride.line.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String type;
    // The user ID is a unique identifier for each user
    // Line use user ID to identify the user
    // Staff use email to identify the user
    private String userId;
}
