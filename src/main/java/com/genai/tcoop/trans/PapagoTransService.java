package com.genai.tcoop.trans;

import com.genai.tcoop.model.constant.Gender;
import com.genai.tcoop.model.dto.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class PapagoTransService {

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;

    public String getTransSentence(String target){

        String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
        String text;
        try {
            text = URLEncoder.encode(target, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("인코딩 실패", e);
        }

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientId);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);

        return post(apiURL, requestHeaders, text);
    }

    public String makePrompt(User user, String address, LocalDateTime time) {
        String prompt = "";
        // 나이 정보 추가
//        prompt += (LocalDateTime.now().getYear() - Integer.parseInt(user.getBirthday()))/10*10 + "olds ";
        prompt += "A happy korean " + (LocalDateTime.now().getYear() -Integer.parseInt(user.getBirthday().split("-")[0])) + " year old ";

        // 성별 정보 추가
        prompt += user.getGender() == Gender.MALE ? "man is " : "woman is ";

        // 여행 중인 정보 추가
        prompt += "traveling in " + address;
        prompt += " on" + time;

        // 자세한 사진 생성을 위한 prompt 개선
        prompt += "realistic, free hands, intricate details";

        return prompt;

//        String prompt = "korean, human, picture, photo, enjoy, travel, tour, trip, funny pose, ";
//        prompt += (LocalDateTime.now().getYear() - Integer.parseInt(user.getBirthday()))/10*10 + "s ";
//        prompt += user.getGender() == Gender.MALE ? "man, " : "woman, ";
//        prompt += "on " + address.replaceAll("\"", "");
//        return prompt;
    }

    private static String post(String apiUrl, Map<String, String> requestHeaders, String text){
        HttpURLConnection con = connect(apiUrl);
        // 원본언어: 한국어 (ko) -> 목적언어: 영어 (en)
        String postParams = "source=ko&target=en&text=" + text;
        try {
            con.setRequestMethod("POST");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            con.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postParams.getBytes());
                wr.flush();
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 응답
                return readBody(con.getInputStream());
            } else {  // 에러 응답
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            String response = responseBody.toString();
            String start = "\"translatedText\":";
            int startIdx = response.indexOf(start) + start.length();
            int endIdx = response.indexOf(",", startIdx);
            return response.substring(startIdx, endIdx).replaceAll("\"", "");
//            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}
