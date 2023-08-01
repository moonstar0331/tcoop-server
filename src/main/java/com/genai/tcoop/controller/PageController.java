package com.genai.tcoop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/my")
    public String my() {
        return "user/my";
    }

    @GetMapping("/planners/{plannerId}")
    public String planner(@PathVariable Long plannerId) {
        return "planner/planner";
    }

    @GetMapping("/planners/{plannerId}/map")
    public String map(@PathVariable Long plannerId) {
        return "planner/map";
    }

    @GetMapping("/planners/list")
    public String myPlanners() {
        return "user/planners";
    }

    @GetMapping("/planners/create")
    public String createPlanner() {
        return "planner/create";
    }

}
