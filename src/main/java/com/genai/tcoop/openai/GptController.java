package com.genai.tcoop.openai;

import com.genai.tcoop.model.dto.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GptController {

    private final GptService gptService;

    @PostMapping("/gpt")
    public Response<KeywordsWithCommentResponse> chat(@RequestBody KeywordRequest request) {
        return Response.success(gptService.callGpt(request.getKeywords()));
    }

    @PostMapping("/gpt/filtering")
    public Response<KeywordsResponse> filtering(@RequestBody KeywordRequest request) {
        List<String> filteredKeywords = gptService.callGptForFiltering(request.getKeywords());
        return Response.success(new KeywordsResponse(filteredKeywords));
    }

    @PostMapping("/gpt/comment")
    public Response<CommentResponse> comment(@RequestBody KeywordRequest request) {
        String comment = gptService.callGptForCreateComment(request.getKeywords());
        return Response.success(new CommentResponse(comment));
    }
}
