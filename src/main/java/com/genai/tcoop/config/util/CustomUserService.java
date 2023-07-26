package com.genai.tcoop.config.util;

import com.genai.tcoop.exception.ErrorCode;
import com.genai.tcoop.exception.TcoopException;
import com.genai.tcoop.model.dto.User;
import com.genai.tcoop.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAccountRepository.findByUserAccountId(username)
                .map(User::fromEntity)
                .orElseThrow(() -> new TcoopException(ErrorCode.USER_NOT_FOUND,
                        String.format("%s not founded", username)));
    }
}
