package com.genai.tcoop.tourapi;

import com.genai.tcoop.model.dto.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
public class TourApiController {

    private final TourApiService tourApiService;

    @PostMapping("/api/tourapi")
    public Response<TourApiInfoListResponse> tourApi(@RequestBody KeywordRequest request) throws UnsupportedEncodingException {
        TourApiInfoListResponse tour = tourApiService.tour(request.getKeywords());
        return Response.success(tour);
    }
}
