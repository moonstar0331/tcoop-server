package com.genai.tcoop.tourapi;

import com.genai.tcoop.model.dto.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class TestController {

    @Value("${tour.api.en.key}")
    private String TOUR_KEY;

    private final RestTemplate restTemplate;

    private final String BASE_URL = "http://apis.data.go.kr/B551011/EngService1/searchKeyword1";

    @GetMapping("/api/test")
    public Response<String> test(Authentication authentication, @RequestParam("keyword") String keyword) {
        String url = "http://apis.data.go.kr/B551011/EngService1/searchKeyword1?serviceKey=" + URLEncoder.encode("TL4U9WIunRjba5IMRgJgTzJPtnlvMr5tM+GZaF0moAtt2ZaP5L3qpU+gad4/fpp58OEzmg3vVTZr1Pu1HWIrpg==", StandardCharsets.UTF_8) + "&numOfRows=10&pageNo=1&MobileOS=ETC&_type=json&MobileApp=AppTest&listYN=Y&keyword=서울역";
        String response = restTemplate.getForObject(url, String.class);

        return Response.success(response);
    }
}
