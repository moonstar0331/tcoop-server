package com.genai.tcoop.controller;

import com.genai.tcoop.model.dto.request.TourRequest;
import com.genai.tcoop.model.dto.response.Response;
import com.genai.tcoop.model.dto.response.TourInfoListResponse;
import com.genai.tcoop.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class TourController {

    private final TourService tourService;

    @GetMapping("/api/tour")
    public Response<TourInfoListResponse> suggest(@RequestBody TourRequest request) throws IOException {
        TourInfoListResponse response = tourService.suggest(request.getMapx(), request.getMapy());
        return Response.success(response);
    }
}
