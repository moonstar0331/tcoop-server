package com.genai.tcoop.model.dto;

import com.genai.tcoop.model.entity.Plan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanDTO {

    private Long id;
    private PlannerDTO planner;
    private String content;
    private String address;
    private String latitude;
    private String longitude;
    private String imageUrl;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;

    public static PlanDTO fromEntity(Plan entity) {
        return PlanDTO.builder()
                .id(entity.getId())
                .planner(PlannerDTO.fromEntity(entity.getPlanner()))
                .content(entity.getContent())
                .address(entity.getAddress())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .imageUrl(entity.getImageUrl())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .isDeleted(entity.getIsDeleted())
                .build();
    }
}
