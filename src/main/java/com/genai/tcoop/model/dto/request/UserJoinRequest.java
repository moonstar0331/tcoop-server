package com.genai.tcoop.model.dto.request;

import com.genai.tcoop.model.constant.Gender;
import lombok.*;

@Getter
@AllArgsConstructor
public class UserJoinRequest {

    private String userAccountId;
    private String password;
    private String name;
    private String nickname;
    private String birthday;
    private Gender gender;
    private String address;
}
