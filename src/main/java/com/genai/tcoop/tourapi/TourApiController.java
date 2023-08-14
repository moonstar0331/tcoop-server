package com.genai.tcoop.tourapi;

import com.genai.tcoop.model.dto.response.Response;
import com.genai.tcoop.openai.GptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TourApiController {

    private final TourApiService tourApiService;
    private final GptService gptService;

    @PostMapping("/api/tourapi")
    public Response<TourApiInfoListResponse> tourApi(@RequestBody KeywordRequest request) throws UnsupportedEncodingException {
        List<String> filteredKeywords = gptService.callGptForTour(request.getKeywords());
        TourApiInfoListResponse tour = tourApiService.tour(filteredKeywords);
        return Response.success(tour);
    }
}
