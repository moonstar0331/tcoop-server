package com.genai.tcoop.model.dto.response;

import com.genai.tcoop.model.dto.PlannerDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlannerResponse {

    private Long plannerId;
    private String title;
    private String userAccountId;

    public static PlannerResponse fromPlannerDto(PlannerDTO dto) {
        return new PlannerResponse(dto.getId(), dto.getTitle(), dto.getUser().getUsername());
    }
}
