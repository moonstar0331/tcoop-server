package com.genai.tcoop.controller;

import com.genai.tcoop.model.dto.PlannerDTO;
import com.genai.tcoop.model.dto.request.PlannerCreateRequest;
import com.genai.tcoop.model.dto.request.PlannerUpdateRequest;
import com.genai.tcoop.model.dto.response.PlannerResponse;
import com.genai.tcoop.model.dto.response.Response;
import com.genai.tcoop.service.PlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners")
public class PlannerController {

    private final PlannerService plannerService;

    @PostMapping
    public Response<PlannerResponse> create(@RequestBody PlannerCreateRequest request, Authentication authentication) {
        PlannerDTO planner = plannerService.create(request.getTitle(), authentication.getName());
        return Response.success(PlannerResponse.fromPlannerDto(planner));
    }

    @PatchMapping("/{plannerId}")
    public Response<PlannerResponse> update(@PathVariable Long plannerId, @RequestBody PlannerUpdateRequest request, Authentication authentication) {
        PlannerDTO planner = plannerService.update(plannerId, request.getTitle(), authentication.getName());
        return Response.success(PlannerResponse.fromPlannerDto(planner));
    }
}
