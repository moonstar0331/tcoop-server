package com.genai.tcoop.controller;

import com.genai.tcoop.model.dto.PlanDTO;
import com.genai.tcoop.model.dto.PlannerDTO;
import com.genai.tcoop.model.dto.request.PlanCreateRequest;
import com.genai.tcoop.model.dto.request.PlannerCreateRequest;
import com.genai.tcoop.model.dto.request.PlannerUpdateRequest;
import com.genai.tcoop.model.dto.response.PlanResponse;
import com.genai.tcoop.model.dto.response.PlannerResponse;
import com.genai.tcoop.model.dto.response.Response;
import com.genai.tcoop.service.PlanService;
import com.genai.tcoop.service.PlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners")
public class PlannerController {

    private final PlannerService plannerService;
    private final PlanService planService;

    @PostMapping
    public Response<PlannerResponse> create(@RequestBody PlannerCreateRequest request, Authentication authentication) {
        PlannerDTO planner = plannerService.create(request.getTitle(), authentication.getName());
        return Response.success(PlannerResponse.fromPlannerDto(planner));
    }

//    @GetMapping("/{plannerId}")
//    public Response<PlannerResponse> get(@PathVariable Long plannerId) {
//        plannerService.get(plannerId);
//    }

    @PatchMapping("/{plannerId}")
    public Response<PlannerResponse> update(@PathVariable Long plannerId, @RequestBody PlannerUpdateRequest request, Authentication authentication) {
        PlannerDTO planner = plannerService.update(plannerId, request.getTitle(), authentication.getName());
        return Response.success(PlannerResponse.fromPlannerDto(planner));
    }

    @DeleteMapping("/{plannerId}")
    public Response<Void> delete(@PathVariable Long plannerId, Authentication authentication) {
        plannerService.delete(plannerId, authentication.getName());
        return Response.success();
    }

    @PostMapping("/{plannerId}/plan")
    public Response<PlanResponse> createPlan(@PathVariable Long plannerId, @RequestBody PlanCreateRequest request, Authentication authentication) throws Exception {
        PlanDTO plan = planService.create(plannerId, request, authentication.getName());
        return Response.success(PlanResponse.fromPlanDto(plan));
    }
}
