package com.genai.tcoop.service;

import com.genai.tcoop.aws.AwsS3Uploader;
import com.genai.tcoop.exception.ErrorCode;
import com.genai.tcoop.exception.TcoopException;
import com.genai.tcoop.karlo.KakaoKarloImageService;
import com.genai.tcoop.model.dto.PlanDTO;
import com.genai.tcoop.model.dto.User;
import com.genai.tcoop.model.dto.request.PlanRequest;
import com.genai.tcoop.model.entity.Plan;
import com.genai.tcoop.model.entity.Planner;
import com.genai.tcoop.model.entity.UserAccount;
import com.genai.tcoop.repository.PlanRepository;
import com.genai.tcoop.repository.PlannerRepository;
import com.genai.tcoop.repository.UserAccountRepository;
import com.genai.tcoop.trans.PapagoTransService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final UserAccountRepository userAccountRepository;
    private final PlanRepository planRepository;
    private final PlannerRepository plannerRepository;

    private final PapagoTransService papagoTransService;
    private final KakaoKarloImageService kakaoKarloImageService;
    private final AwsS3Uploader awsS3Uploader;

    @Transactional
    public PlanDTO create(Long plannerId, PlanRequest request, String userAccountId) throws Exception {
        // planner 존재 여부 validation
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new TcoopException(ErrorCode.PLANNER_NOT_FOUND));

        // 유저 존재 여부 validation
        UserAccount user = userAccountRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> new TcoopException(ErrorCode.USER_NOT_FOUND, String.format("%s is not found", userAccountId)));

        // planner 작성자와 로그인 사용자 validation
        if (planner.getUser() != user) {
            throw new TcoopException(ErrorCode.INVALID_PERMISSION,
                    String.format("%s has no permission with %s planner", userAccountId, plannerId));
        }

        // Papago 번역 API 호출 : Naver 지역 한글 정보 -> 영어
        String transAddress = papagoTransService.getTransSentence(request.getAddress());

        // prompt 생성
        String prompt = papagoTransService.makePrompt(User.fromEntity(user), transAddress, request.getStartTime());

        // Kakao Karlo API 호출 및 이미지 AWS S3에 저장
        String imageUrl = kakaoKarloImageService.create(prompt);

        // plan 생성 및 저장
        Plan saved = planRepository.save(Plan.builder()
                .planner(planner)
                .content(request.getContent())
                .address(request.getAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .imageUrl(imageUrl)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build());

        return PlanDTO.fromEntity(saved);
    }

    public PlanDTO get(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new TcoopException(ErrorCode.PLAN_NOT_FOUND));

        return PlanDTO.fromEntity(plan);
    }

    @Transactional
    public PlanDTO update(Long planId, PlanRequest request, String userAccountId) throws Exception {
        // Plan 존재 여부 validation
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new TcoopException(ErrorCode.PLAN_NOT_FOUND));

        // 사용자 존재 여부 validation
        UserAccount user = userAccountRepository.findByUserAccountId(userAccountId)
                .orElseThrow(() -> new TcoopException(ErrorCode.USER_NOT_FOUND, String.format("%s is not found", userAccountId)));

        // Planner 작성자와 로그인 사용자 validation

        if (plan.getPlanner().getUser() != user) {
            throw new TcoopException(ErrorCode.INVALID_PERMISSION,
                    String.format("%s has no permission with %s plan", userAccountId, planId));
        }

        // 주소 변경 -> 이미지 새로 생성
        if(!plan.getAddress().equals(request.getAddress())) {
            String beforeImageUrl = plan.getImageUrl();

            // 이미지 새로 생성
            // Papago 번역 API 호출 : Naver 지역 한글 정보 -> 영어
            String transAddress = papagoTransService.getTransSentence(request.getAddress());

            // prompt 생성
            String prompt = papagoTransService.makePrompt(User.fromEntity(user), transAddress, request.getStartTime());

            // Kakao Karlo API 호출 및 이미지 AWS S3에 저장
            String imageUrl = kakaoKarloImageService.create(prompt);

            if(!beforeImageUrl.equals(imageUrl) && imageUrl != null) {
                // AWS S3 기존 image 삭제
                awsS3Uploader.deleteImage(beforeImageUrl);

                // 새로 생성된 이미지로 변경
                plan.updateImageUrl(imageUrl);
            }
            plan.updateAddress(request.getAddress());
        }

        // 나머지 정보 변경
        plan.updatePlan(request.getContent(), request.getLatitude(), request.getLongitude(),
                request.getStartTime(), request.getEndTime());

        // 변경사항 저장
        Plan updated = planRepository.save(plan);
        return PlanDTO.fromEntity(updated);
    }
}
