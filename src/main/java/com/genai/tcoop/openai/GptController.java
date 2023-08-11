package com.genai.tcoop.openai;

import com.genai.tcoop.model.dto.response.Response;
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

    @PostMapping("/gpt")
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

//        return prompt + "중 여행 관련된 키워드를 필터링해서 여행 일정을 계획해줘.";
        return prompt + "중에서 '여행' 단어 자체는 제외하고 여행 테마나 장소와 관련된 단어만을 키워드로 사용하여 여행일정 추천해줘. ";
    }
}
