package com.genai.tcoop.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PlannerGetResponse {
    private PlannerResponse planner;
    private List<PlanResponse> plans;
}
