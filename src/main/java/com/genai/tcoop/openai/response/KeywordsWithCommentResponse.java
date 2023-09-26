package com.genai.tcoop.openai.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KeywordsWithCommentResponse {

    private List<String> keywords;
    private String comment;
}
