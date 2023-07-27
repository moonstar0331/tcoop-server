package com.genai.tcoop.controller;

import com.genai.tcoop.model.dto.request.PlannerCreateRequest;
import com.genai.tcoop.model.dto.response.Response;
import com.genai.tcoop.service.PlannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/planners")
public class PlannerController {

    private final PlannerService plannerService;

    @PostMapping
    public Response<Void> create(@RequestBody PlannerCreateRequest request, Authentication authentication) {
        plannerService.create(request.getTitle(), authentication.getName());
        return Response.success();
    }
}
