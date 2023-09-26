package com.genai.tcoop.openai.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPromptRequest {
    private List<String> keywords;
    private String prompt;
}
