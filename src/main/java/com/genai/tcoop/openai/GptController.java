package com.genai.tcoop.openai;

import com.genai.tcoop.model.dto.response.Response;
import com.genai.tcoop.openai.request.KeywordRequest;
import com.genai.tcoop.openai.request.UserPromptRequest;
import com.genai.tcoop.openai.response.CommentResponse;
import com.genai.tcoop.openai.response.KeywordsResponse;
import com.genai.tcoop.openai.response.KeywordsWithCommentResponse;
import com.genai.tcoop.openai.response.PromptContentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/gpt")
@RequiredArgsConstructor
public class GptController {

    private final GptService gptService;

    @PostMapping
    public Response<KeywordsWithCommentResponse> chat(@RequestBody KeywordRequest request) {
        return Response.success(gptService.callGpt(request.getKeywords()));
    }

    @PostMapping("/filtering")
    public Response<KeywordsResponse> filtering(@RequestBody KeywordRequest request) {
        log.info("before filtering: {}", request.getKeywords());
        List<String> filteredKeywords = gptService.callGptForFiltering(request.getKeywords());
        log.info("after filtering: {}", filteredKeywords);
        return Response.success(new KeywordsResponse(filteredKeywords));
    }

    @PostMapping("/comment")
    public Response<CommentResponse> comment(@RequestBody KeywordRequest request) {
        String comment = gptService.callGptForCreateComment(request.getKeywords());
        log.info("Created comment: {}", comment);
        return Response.success(new CommentResponse(comment));
    }

    @PostMapping("/prompt")
    public Response<PromptContentResponse> filteringWithPrompt(@RequestBody UserPromptRequest request) {
        String gptContent = gptService.callGptWithPrompt(request.getKeywords(), request.getPrompt());
        return Response.success(new PromptContentResponse(gptContent));
    }
}
