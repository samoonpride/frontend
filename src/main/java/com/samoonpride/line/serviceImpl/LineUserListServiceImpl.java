package com.samoonpride.line.serviceImpl;

import com.samoonpride.line.dto.UserDto;
import com.samoonpride.line.service.LineUserListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LineUserListServiceImpl implements LineUserListService {
    private final List<UserDto> userDtoList = new ArrayList<>();
    private final LineUserServiceImpl lineUserService;

    @Override
    public void addLineUser(UserDto userDto) {
        if (userDtoList.contains(userDto)) {
            return;
        }
        userDtoList.add(userDto);
    }

    @Override
    public UserDto findLineUser(String userId) {
        return userDtoList.stream()
                .filter(userDto -> userDto.getKey().equals(userId))
                .findFirst()
                .orElseGet(() -> {
                    UserDto userDto = lineUserService.createLineUser(userId);
                    this.addLineUser(userDto);
                    return userDto;
                });
    }
}
