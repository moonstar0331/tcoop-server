package com.genai.tcoop.search;

import com.genai.tcoop.model.dto.response.Response;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public Response<NaverLocationInfoListResponse> searchPlace(@RequestParam("keyword") @NotBlank String keyword)
            throws IOException, ParseException {
        return Response.success(searchService.searchLocation(keyword));
    }
}
