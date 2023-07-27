package com.genai.tcoop.service;

import com.genai.tcoop.config.util.CustomUserService;
import com.genai.tcoop.exception.ErrorCode;
import com.genai.tcoop.exception.TcoopException;
import com.genai.tcoop.model.dto.User;
import com.genai.tcoop.model.dto.request.UserJoinRequest;
import com.genai.tcoop.model.entity.UserAccount;
import com.genai.tcoop.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final CustomUserService customUserService;
    private final PasswordEncoder passwordEncoder;
    private final UserAccountRepository userAccountRepository;

    @Transactional
    public User join(UserJoinRequest request) {
        userAccountRepository.findByUserAccountId(request.getUserAccountId()).ifPresent(u -> {
            throw new TcoopException(ErrorCode.DUPLICATED_USER_ACCOUNT_ID, String.format("%s is duplicated", request.getUserAccountId()));
        });

        UserAccount user = userAccountRepository.save(UserAccount.builder()
                .userAccountId(request.getUserAccountId())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .nickname(request.getNickname())
                .birthday(request.getBirthday())
                .gender(request.getGender())
                .address(request.getAddress())
                .build());

        return User.fromEntity(user);
    }
}
