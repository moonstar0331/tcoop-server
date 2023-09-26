package com.genai.tcoop.openai.request;

import com.genai.tcoop.openai.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

    private String model;
    private List<Message> messages;
    private int n;
    private double temperature;

    public ChatRequest(String model, String prompt) {
        this.model = model;
        this.n = 1;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", prompt));
    }
}
