package com.samoonpride.line.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLineUserRequest {
    private String userId;
    private String displayName;
}
