package com.genai.tcoop.model.dto;


import com.genai.tcoop.model.entity.Planner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlannerDTO {

    private Long id;
    private User user;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;

    public static PlannerDTO fromEntity(Planner entity) {
        return PlannerDTO.builder()
                .id(entity.getId())
                .user(User.fromEntity(entity.getUser()))
                .title(entity.getTitle())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .isDeleted(entity.getIsDeleted())
                .build();
    }
}
