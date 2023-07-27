package com.genai.tcoop.service;

import com.genai.tcoop.exception.ErrorCode;
import com.genai.tcoop.exception.TcoopException;
import com.genai.tcoop.model.dto.PlannerDTO;
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
    public PlannerDTO create(String title, String userAccountId) {
        // 사용자 존재 여부 validation
        UserAccount user = userAccountRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> new TcoopException(ErrorCode.USER_NOT_FOUND, String.format("%s is not found", userAccountId)));

        // planner 생성
        Planner saved = plannerRepository.save(Planner.builder()
                .user(user)
                .title(title)
                .build());

        // entity -> dto
        return PlannerDTO.fromEntity(saved);
    }

    @Transactional
    public PlannerDTO update(Long plannerId, String title, String userAccountId) {
        // 사용자 존재 여부 validation
        UserAccount user = userAccountRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> new TcoopException(ErrorCode.USER_NOT_FOUND, String.format("%s is not found", userAccountId)));

        // planner 존재 여부 validation
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new TcoopException(ErrorCode.PLANNER_NOT_FOUND));

        // planner 작성자와 로그인 사용자 validation
        if(planner.getUser() != user) {
            throw new TcoopException(ErrorCode.INVALID_PERMISSION,
                    String.format("%s has no permission with %s", userAccountId, plannerId));
        }

        // planner 수정
        planner.updateTitle(title);
        Planner updated = plannerRepository.save(planner);

        return PlannerDTO.fromEntity(updated);
    }
}
