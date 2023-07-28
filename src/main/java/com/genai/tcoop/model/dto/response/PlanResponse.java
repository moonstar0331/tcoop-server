package com.genai.tcoop.model.dto.response;

import com.genai.tcoop.model.dto.PlanDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PlanResponse {

    private Long id;
    private Long plannerId;
    private String content;
    private String address;
    private Double latitude;
    private Double longitude;
    private String imageUrl;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PlanResponse fromPlanDto(PlanDTO dto) {
        return new PlanResponse(dto.getId(), dto.getPlanner().getId(), dto.getContent(), dto.getAddress(),
                dto.getLatitude(), dto.getLongitude(), dto.getImageUrl(), dto.getStartTime(), dto.getEndTime(),
                dto.getCreatedAt(), dto.getUpdatedAt());
    }
}
