package com.genai.tcoop.openai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GptService {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String url;

    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;
    
    public KeywordsWithCommentResponse callGpt(List<String> keywords) {
        
        // filtering gpt api 호출
        log.info("before filtering: {}", keywords);
        List<String> filteredKeywords = callGptForFiltering(keywords);
        log.info("after filtering: {}", filteredKeywords);
        
        // filtered keywords 를 사용해서 comment create gpt api 호출
        String createdComment = callGptForCreateComment(filteredKeywords);

        // filtering 결과 + comment create 결과 리턴
        return new KeywordsWithCommentResponse(filteredKeywords, createdComment);
    }

    public List<String> callGptForFiltering(List<String> keywords) {
        String prompt = generateFilteringPrompt(keywords);

        ChatRequest chatRequest = new ChatRequest(model, prompt);

        ChatResponse chatResponse = restTemplate.postForObject(url, chatRequest, ChatResponse.class);

        if(chatResponse == null || chatResponse.getChoices() == null || chatResponse.getChoices().isEmpty()) {
            return List.of();
        }

        String content = chatResponse.getChoices().get(0).getMessage().getContent();
        List<String> filteredKeywords = Arrays.asList(content.split(", "));
        
        return filteredKeywords;
    }

    private String generateFilteringPrompt(List<String> keywords) {
        String prompt = "";

        for(int i=0; i<keywords.size(); i++) {
            if(i == keywords.size()-1) {
                prompt += keywords.get(i) + " ";
            } else {
                prompt += keywords.get(i) + ", ";
            }
        }

        return prompt + "중에서 여행 테마나 장소와 관련된 고유명사로만 필터링 해서 설명 없이 결과만 콤마로 구분해줘.";
    }

    public String callGptForCreateComment(List<String> keywords) {
        if(keywords.size() == 0 || keywords.isEmpty()) {
            return "Filtered keyword list is empty";
        }

        String prompt = generatePromptForComment(keywords);

        ChatRequest chatRequest = new ChatRequest(model, prompt);

        ChatResponse chatResponse = restTemplate.postForObject(url, chatRequest, ChatResponse.class);

        if(chatResponse == null || chatResponse.getChoices() == null || chatResponse.getChoices().isEmpty()) {
            return "No response";
        }

        return chatResponse.getChoices().get(0).getMessage().getContent();
    }

    private String generatePromptForComment(List<String> keywords) {
        String prompt = "";

        for(int i=0; i<keywords.size(); i++) {
            if(i == keywords.size()-1) {
                prompt += keywords.get(i) + " ";
            } else {
                prompt += keywords.get(i) + ", ";
            }
        }

        return prompt + "를 이용해서 여행 일정을 계획해줘.";
    }
}
