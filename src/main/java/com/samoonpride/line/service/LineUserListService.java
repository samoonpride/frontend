package com.samoonpride.line.service;

import com.samoonpride.line.dto.UserDto;

public interface LineUserListService {
    UserDto findLineUser(String userId);

    void addLineUser(UserDto userDto);
}
