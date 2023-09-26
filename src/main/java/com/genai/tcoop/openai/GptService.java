package com.genai.tcoop.openai;

import com.genai.tcoop.openai.request.ChatRequest;
import com.genai.tcoop.openai.response.KeywordsWithCommentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

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

    // Google Vision API 로 추출된 키워드 리스트를 추출하기 위한 메소드
    public List<String> callGptForFiltering(List<String> keywords) {

        if(keywords.isEmpty()) {
            return List.of();
        }

        String prompt = generateFilteringPrompt(keywords);

        ChatRequest chatRequest = new ChatRequest(model, prompt);

        ChatResponse chatResponse = restTemplate.postForObject(url, chatRequest, ChatResponse.class);

        if(chatResponse == null || chatResponse.getChoices() == null || chatResponse.getChoices().isEmpty()) {
            return List.of();
        }

        String content = chatResponse.getChoices().get(0).getMessage().getContent();

        log.info("GPT filtered Keywords: {}", content);

        List<String> filteredKeywords = Arrays.asList(content.split(", "));

        // GPT 응답이 리스트 형식이 아닐 경우에 대한 대비
        String lastKeyword = filteredKeywords.get(filteredKeywords.size() - 1);
        int index = lastKeyword.indexOf("중에서");
        if(index != -1) {
            String modified = lastKeyword.substring(0, index-1);
            filteredKeywords.set(filteredKeywords.size()-1, modified);
        }

        return filteredKeywords;
    }

    // GPT 를 통해서 키워드 필터링을 위한 프롬프트 생성 메소드
    private String generateFilteringPrompt(List<String> keywords) {
        String prompt = "";

        for(int i=0; i<keywords.size(); i++) {
            if(i == keywords.size()-1) {
                prompt += keywords.get(i) + " ";
            } else {
                prompt += keywords.get(i) + ", ";
            }
        }

        return prompt + "중에서 여행 장소나 지역과 관련된 키워드만 필터링해서 최대 10개의 키워드를 설명 없이 결과만 콤마로 구분해줘.";
    }

    // 필터링된 키워드를 기반으로 GPT 를 통해서 자동으로 작성할 댓글이 생성되는 메소드
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

    // GPT 를 통한 댓글 자동 생성을 위한 프롬프트 생성 메소드
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

    public String callGptWithPrompt(List<String> keywords, String prompt) {

        if(keywords.size() == 0 || keywords.isEmpty()) {
            return "Input keyword List is Empty";
        }

        String gptPrompt = keywords + prompt;
        log.info("callGptWithPrompt keyword: {}", keywords);

        ChatRequest chatRequest = new ChatRequest(model, gptPrompt);

        ChatResponse chatResponse = restTemplate.postForObject(url, chatRequest, ChatResponse.class);

        if (chatResponse == null || chatResponse.getChoices() == null || chatResponse.getChoices().isEmpty()) {
            return "No response";
        }

        return chatResponse.getChoices().get(0).getMessage().getContent();
    }
}
