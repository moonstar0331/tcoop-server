package com.genai.tcoop.service;

import com.genai.tcoop.config.util.CustomUserService;
import com.genai.tcoop.config.util.JwtTokenUtils;
import com.genai.tcoop.exception.ErrorCode;
import com.genai.tcoop.exception.TcoopException;
import com.genai.tcoop.model.dto.User;
import com.genai.tcoop.model.dto.request.UserJoinRequest;
import com.genai.tcoop.model.entity.UserAccount;
import com.genai.tcoop.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final CustomUserService customUserService;
    private final PasswordEncoder passwordEncoder;
    private final UserAccountRepository userAccountRepository;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

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

    public String login(String userAccountId, String password) {
        // 회원가입 여부 체크
        User user = customUserService.loadUserByUsername(userAccountId);

        // 비밀번호 체크
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new TcoopException(ErrorCode.INVALID_PASSWORD);
        }

        // jwt 토큰 생성
        return JwtTokenUtils.generateToken(userAccountId, secretKey, expiredTimeMs);
    }
}
