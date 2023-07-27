package com.genai.tcoop.service;

import com.genai.tcoop.exception.ErrorCode;
import com.genai.tcoop.exception.TcoopException;
import com.genai.tcoop.model.entity.Planner;
import com.genai.tcoop.model.entity.UserAccount;
import com.genai.tcoop.repository.PlannerRepository;
import com.genai.tcoop.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlannerService {

    private final UserAccountRepository userAccountRepository;
    private final PlannerRepository plannerRepository;

    @Transactional
    public void create(String title, String userAccountId) {
        // 사용자 validation
        UserAccount user = userAccountRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> new TcoopException(ErrorCode.USER_NOT_FOUND, String.format("%s is not found", userAccountId)));

        // planner 생성
        plannerRepository.save(Planner.builder()
                .user(user)
                .title(title)
                .build());
    }
}
