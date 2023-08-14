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


    public String callGpt(List<String> keywords) {
        log.info("callGpt: {}", keywords.toString());
        String prompt = generatePrompt(keywords);

        ChatRequest chatRequest = new ChatRequest(model, prompt);

        ChatResponse chatResponse = restTemplate.postForObject(url, chatRequest, ChatResponse.class);

        if(chatResponse == null || chatResponse.getChoices() == null || chatResponse.getChoices().isEmpty()) {
            return "No response";
        }

        return chatResponse.getChoices().get(0).getMessage().getContent();
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

        return prompt + "중에서 여행 테마나 장소 관련한 고유명사를 키워드로 추출해서 여행 일정을 계획해줘.";
    }

    public List<String> callGptForTour(List<String> keywords) {
        log.info("callGptForTour: {}", keywords.toString());
        String prompt = generatePromptForTourApi(keywords);

        ChatRequest chatRequest = new ChatRequest(model, prompt);

        ChatResponse chatResponse = restTemplate.postForObject(url, chatRequest, ChatResponse.class);

        if(chatResponse == null || chatResponse.getChoices() == null || chatResponse.getChoices().isEmpty()) {
            return List.of();
        }

        String content = chatResponse.getChoices().get(0).getMessage().getContent();
        log.info("\ncontent: \n{}", content);

        List<String> filteredKeywords = parsingKeyword(content);
        log.info("filteredKeywords: {}", filteredKeywords.toString());

        return filteredKeywords;
    }

    private String generatePromptForTourApi(List<String> keywords) {
        String prompt = "";

        for(int i=0; i<keywords.size(); i++) {
            if(i == keywords.size()-1) {
                prompt += keywords.get(i) + " ";
            } else {
                prompt += keywords.get(i) + ", ";
            }
        }

        return prompt + "중에서 여행 테마나 장소와 관련된 고유명사를 키워드로 추출해서 리스트 형식으로 반환해줘.";
    }

    private List<String> parsingKeyword(String content) {
        String[] split = content.split("- ");
        for(int i=1; i<split.length; i++) {
            int idx = split[i].indexOf('(');
            if (idx == -1) {
                split[i] = split[i].trim();
            } else {
                split[i] = split[i].substring(0, idx-1).trim();;
            }
        }

        return Arrays.stream(split, 1, split.length).collect(Collectors.toList());
    }
}
