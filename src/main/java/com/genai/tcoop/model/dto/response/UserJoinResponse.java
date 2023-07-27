package com.genai.tcoop.model.dto.response;

import com.genai.tcoop.model.constant.Gender;
import com.genai.tcoop.model.constant.UserRole;
import com.genai.tcoop.model.dto.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserJoinResponse {

    private Long id;
    private String userAccountId;
    private UserRole role;
    private String name;
    private String nickname;
    private String birthday;
    private Gender gender;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserJoinResponse fromUser(User user) {
        return new UserJoinResponse(
                user.getId(),
                user.getUsername(),
                user.getUserRole(),
                user.getName(),
                user.getNickname(),
                user.getBirthday(),
                user.getGender(),
                user.getAddress(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
