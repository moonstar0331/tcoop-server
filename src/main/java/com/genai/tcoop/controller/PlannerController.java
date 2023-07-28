package com.genai.tcoop.controller;

import com.genai.tcoop.model.dto.PlanDTO;
import com.genai.tcoop.model.dto.PlannerDTO;
import com.genai.tcoop.model.dto.request.PlanRequest;
import com.genai.tcoop.model.dto.request.PlannerRequest;
import com.genai.tcoop.model.dto.response.PlanResponse;
import com.genai.tcoop.model.dto.response.PlannerGetResponse;
import com.genai.tcoop.model.dto.response.PlannerResponse;
import com.genai.tcoop.model.dto.response.Response;
import com.genai.tcoop.service.PlanService;
import com.genai.tcoop.service.PlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners")
public class PlannerController {

    private final PlannerService plannerService;
    private final PlanService planService;

    @PostMapping
    public Response<PlannerResponse> create(@RequestBody PlannerRequest request, Authentication authentication) {
        PlannerDTO planner = plannerService.create(request.getTitle(), authentication.getName());
        return Response.success(PlannerResponse.fromPlannerDto(planner));
    }

    @GetMapping("/{plannerId}")
    public Response<PlannerGetResponse> get(@PathVariable Long plannerId) {
        PlannerDTO planner = plannerService.get(plannerId);
        List<PlanDTO> plans = planService.getAll(plannerId);
        PlannerGetResponse response = new PlannerGetResponse(PlannerResponse.fromPlannerDto(planner),
                plans.stream().map(PlanResponse::fromPlanDto).collect(Collectors.toList()));
        return Response.success(response);
    }

    @PatchMapping("/{plannerId}")
    public Response<PlannerResponse> update(@PathVariable Long plannerId, @RequestBody PlannerRequest request, Authentication authentication) {
        PlannerDTO planner = plannerService.update(plannerId, request.getTitle(), authentication.getName());
        return Response.success(PlannerResponse.fromPlannerDto(planner));
    }

    @DeleteMapping("/{plannerId}")
    public Response<Void> delete(@PathVariable Long plannerId, Authentication authentication) {
        plannerService.delete(plannerId, authentication.getName());
        return Response.success();
    }

    @PostMapping("/{plannerId}/plans")
    public Response<PlanResponse> createPlan(@PathVariable Long plannerId, @RequestBody PlanRequest request, Authentication authentication) throws Exception {
        PlanDTO plan = planService.create(plannerId, request, authentication.getName());
        return Response.success(PlanResponse.fromPlanDto(plan));
    }

    @GetMapping("/plans/{planId}")
    public Response<PlanResponse> getPlan(@PathVariable Long planId) {
        PlanDTO plan = planService.get(planId);
        return Response.success(PlanResponse.fromPlanDto(plan));
    }

    @PatchMapping("/plans/{planId}")
    public Response<PlanResponse> updatePlan(@PathVariable Long planId, @RequestBody PlanRequest request, Authentication authentication) throws Exception {
        PlanDTO plan = planService.update(planId, request, authentication.getName());
        return Response.success(PlanResponse.fromPlanDto(plan));
    }

    @DeleteMapping("/plans/{planId}")
    public Response<Void> deletePlan(@PathVariable Long planId, Authentication authentication) {
        planService.delete(planId, authentication.getName());
        return Response.success();
    }
}
