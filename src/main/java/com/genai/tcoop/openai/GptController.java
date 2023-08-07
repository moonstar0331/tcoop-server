package com.genai.tcoop.openai;

import com.genai.tcoop.model.dto.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GptController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String url;

    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/gpt")
    public Response<String> chat(@RequestBody KeywordRequest request) throws URISyntaxException {
        String prompt = generatePrompt(request.getKeywords());
        ChatRequest chatRequest = new ChatRequest(model, prompt);

        ChatResponse chatResponse = restTemplate.postForObject(url, chatRequest, ChatResponse.class);

        if(chatResponse == null || chatResponse.getChoices() == null || chatResponse.getChoices().isEmpty()) {
            return Response.success("No response");
        }

        return Response.success(chatResponse.getChoices().get(0).getMessage().getContent());
    }

    private String generatePrompt(List<String> keywords) {
        String prompt = "";

        for(int i=0; i<keywords.size(); i++) {
            if(i == keywords.size()-1) {
                prompt += keywords.get(i) + " ";
            } else {
                prompt += keywords.get(i) + ", ";
            }
        }

        return prompt + "중에 관광지와 관련있는 정보를 작성해줘.";
    }
}