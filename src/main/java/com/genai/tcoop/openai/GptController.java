package com.genai.tcoop.openai;

import com.genai.tcoop.model.dto.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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
        log.info("before filtering: {}", request.getKeywords());
        List<String> filteredKeywords = gptService.callGptForFiltering(request.getKeywords());
        log.info("after filtering: {}", filteredKeywords);
        return Response.success(new KeywordsResponse(filteredKeywords));
    }

    @PostMapping("/gpt/comment")
    public Response<CommentResponse> comment(@RequestBody KeywordRequest request) {
        String comment = gptService.callGptForCreateComment(request.getKeywords());
        log.info("Created comment: {}", comment);
        return Response.success(new CommentResponse(comment));
    }
}
