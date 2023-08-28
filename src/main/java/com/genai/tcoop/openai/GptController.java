package com.genai.tcoop.openai;

import com.genai.tcoop.model.dto.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GptController {

    private final GptService gptService;

    @PostMapping("/gpt")
    public Response<KeywordsWithCommentResponse> chat(@RequestBody KeywordRequest request) {
        return Response.success(gptService.callGpt(request.getKeywords()));
    }
}
