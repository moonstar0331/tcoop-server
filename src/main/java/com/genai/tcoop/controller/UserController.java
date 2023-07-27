package com.genai.tcoop.controller;

import com.genai.tcoop.model.dto.User;
import com.genai.tcoop.model.dto.request.UserJoinRequest;
import com.genai.tcoop.model.dto.request.UserLoginRequest;
import com.genai.tcoop.model.dto.response.Response;
import com.genai.tcoop.model.dto.response.UserJoinResponse;
import com.genai.tcoop.model.dto.response.UserLoginResponse;
import com.genai.tcoop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request);
        return Response.success(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getUserAccountId(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }
}
